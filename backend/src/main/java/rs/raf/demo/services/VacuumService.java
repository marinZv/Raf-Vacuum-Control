package rs.raf.demo.services;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import rs.raf.demo.domain.dto.vacuum.VacuumDto;
import rs.raf.demo.domain.dto.vacuum.VacuumErrorDto;
import rs.raf.demo.domain.dto.vacuum.VacuumQueueDto;
import rs.raf.demo.domain.dto.vacuum.VacuumScheduleDto;
import rs.raf.demo.domain.entities.user.RoleType;
import rs.raf.demo.domain.entities.user.User;
import rs.raf.demo.domain.entities.vacuum.*;
import rs.raf.demo.domain.exceptions.NotFoundException;
import rs.raf.demo.domain.exceptions.VacuumException;
import rs.raf.demo.domain.mapper.VacuumMapper;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.repositories.VacuumErrorRepository;
import rs.raf.demo.repositories.VacuumRepository;
//import rs.raf.demo.repositories.VacuumScheduleRepository;
import rs.raf.demo.utils.PermissionUtils;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacuumService {

    private final VacuumRepository vacuumRepository;
    private final VacuumErrorRepository vacuumErrorRepository;
    private final UserRepository userRepository;
    private final AmqpTemplate rabbitTemplate;
    private final TaskScheduler taskScheduler;

    private String email = new String();
    private boolean isScheduled = false;


    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, VacuumErrorRepository vacuumErrorRepository, UserRepository userRepository, AmqpTemplate rabbitTemplate, TaskScheduler taskScheduler) {
        this.vacuumRepository = vacuumRepository;
        this.vacuumErrorRepository = vacuumErrorRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.taskScheduler = taskScheduler;
    }

    @Transactional
    public List<VacuumDto> searchVacuums(String name, List<VacuumStatus> statusList, Long dateFrom, Long dateTo){
        PermissionUtils.checkRole(RoleType.CAN_SEARCH_VACUUM);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Date dateFromParam;
        if(dateFrom == null){
            dateFromParam = null;
        }else{
            dateFromParam = Date.from(Instant.ofEpochMilli(dateFrom));
        }

        Date dateToParam;
        if(dateTo == null){
            dateToParam = null;
        }else{
            dateToParam = Date.from(Instant.ofEpochMilli(dateTo));
        }

        List<Vacuum> vacuums = vacuumRepository.findAllVacuumsByParameters(email, name, statusList, dateFromParam, dateToParam);

        return vacuums.stream().map(VacuumMapper.INSTANCE::vacuumToVacuumDto).collect(Collectors.toList());
    }

    @Transactional
    public VacuumDto addVacuum(String name){
        PermissionUtils.checkRole(RoleType.CAN_ADD_VACUUM);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));

        Vacuum vacuum = new Vacuum();
        vacuum.setName(name);
        vacuum.setCreatedBy(user);
        vacuum.setCreatedDate(Date.from(Instant.now()));

        return VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuumRepository.save(vacuum));
    }

    @Transactional
    public VacuumDto removeVacuum(Long id){
        PermissionUtils.checkRole(RoleType.CAN_REMOVE_VACUUM);

        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(() -> new NotFoundException("invalid vacuum id"));

        if(vacuum.isBusy())
            throw new VacuumException("vacuum is busy");

        if(vacuum.getStatus() != VacuumStatus.STOPPED){
            throw new VacuumException("vacuum is not stopped");
        }

        try{
            vacuum.setActive(false);
            return VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuumRepository.save(vacuum));
        }catch (ObjectOptimisticLockingFailureException e){
            throw new VacuumException("Someone has overtaken you in action");
        }

    }


    public List<VacuumErrorDto> getErrors(){

        List<VacuumError> errors = vacuumErrorRepository.findAll();

        return errors.stream().map(VacuumMapper.INSTANCE::vacuumErrorToVacuumErrorDto).collect(Collectors.toList());
    }

    public void startVacuum(Long id){

        if(isScheduled){
            performAnActionForVacuum(id, VacuumStatus.STOPPED, VacuumAction.START, email);
            isScheduled = false;
        }else{
            PermissionUtils.checkRole(RoleType.CAN_START_VACUUM);
            performAnActionForVacuum(id, VacuumStatus.STOPPED, VacuumAction.START, SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    public void stopVacuum(Long id){
        if(isScheduled){
            performAnActionForVacuum(id, VacuumStatus.RUNNING, VacuumAction.STOP, email);
            isScheduled = false;
        }else{
            PermissionUtils.checkRole(RoleType.CAN_STOP_VACUUM);
            performAnActionForVacuum(id, VacuumStatus.RUNNING, VacuumAction.STOP, SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    public void dischargeVacuum(Long id){
        if(isScheduled){
            performAnActionForVacuum(id, VacuumStatus.STOPPED, VacuumAction.DISCHARGE, email);
            isScheduled = false;
        }else{
            PermissionUtils.checkRole(RoleType.CAN_DISCHARGE_VACUUM);
            performAnActionForVacuum(id, VacuumStatus.STOPPED, VacuumAction.DISCHARGE, SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    @Transactional
    public void performAnActionForVacuum(Long id, VacuumStatus requiredStatus, VacuumAction action, String email){
        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(()-> new NotFoundException("Invalid vacuum id"));

        VacuumError vacuumError = new VacuumError();
        Date date = Date.from(Instant.now());


        if(vacuum.isBusy()){
            vacuumError.setMessage("vacuum is busy");
            vacuumError.setAction(action);
            vacuumError.setVacuum(vacuum);
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("vacuum is busy");
        }

        if(vacuum.getStatus() != requiredStatus){
            vacuumError.setMessage("vacuum is not " + requiredStatus.name().toLowerCase() + ", is " + vacuum.getStatus().name().toLowerCase());
            vacuumError.setAction(action);
            vacuumError.setVacuum(vacuum);
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("vacuum is not " + requiredStatus.name().toLowerCase() + ", is " + vacuum.getStatus().name().toLowerCase());
        }

        try{
            vacuum.setBusy(true);
            vacuumRepository.saveAndFlush(vacuum);
        }catch (ObjectOptimisticLockingFailureException e){
            vacuumError.setMessage("Someone has overtaken you in action");
            vacuumError.setAction(action);
            vacuumError.setVacuum(vacuum);
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("Someone has overtaken you in action");
        }

        sendToQueue(vacuum.getId(), email, action);
    }

    @Transactional(dontRollbackOn = VacuumException.class)
    public void scheduleTaskForVacuum(VacuumScheduleDto vacuumScheduleDto){

        isScheduled = true;
        email = SecurityContextHolder.getContext().getAuthentication().getName();



        Date scheduledDate = new Date(vacuumScheduleDto.getScheduleDate());
        Date now = new Date();

        Vacuum vacuum = vacuumRepository.findById(vacuumScheduleDto.getId()).orElseThrow(()-> new NotFoundException("Invalid vacuum id"));


        if(scheduledDate.before(now)){

            System.out.println("Greska sa zakazivanjem!");

            VacuumError vacuumError = new VacuumError();
            vacuumError.setMessage("You cannot schedule vacuum action in past!");
            vacuumError.setAction(vacuumScheduleDto.getAction());
            vacuumError.setVacuum(vacuum);
            vacuumError.setDateError(now);

            System.out.println(vacuumError);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("You cannot schedule vacuum action in past!");
        }

        taskScheduler.schedule(()-> {

            if(vacuumScheduleDto.getAction() == VacuumAction.START){
                System.out.println("isScheduled: " + isScheduled);
                startVacuum(vacuumScheduleDto.getId());
            }else if(vacuumScheduleDto.getAction() == VacuumAction.STOP){
                stopVacuum(vacuumScheduleDto.getId());
            }else if(vacuumScheduleDto.getAction() == VacuumAction.DISCHARGE) {
                dischargeVacuum(vacuumScheduleDto.getId());
            }

            }, scheduledDate);
    }

    public void sendToQueue(Long id, String email, VacuumAction vacuumAction){

        VacuumQueueDto vacuumQueueDto = new VacuumQueueDto();
        vacuumQueueDto.setId(id);
        vacuumQueueDto.setEmail(email);
        vacuumQueueDto.setVacuumAction(vacuumAction);

        System.out.println("Pre slanja na vacuumQueue");
        rabbitTemplate.convertAndSend("vacuumQueue", vacuumQueueDto);
        System.out.println("Posle slanja na vacuumQueue");
    }

}
