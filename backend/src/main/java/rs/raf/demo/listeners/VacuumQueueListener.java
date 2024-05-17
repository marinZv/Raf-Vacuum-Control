package rs.raf.demo.listeners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import rs.raf.demo.domain.dto.vacuum.VacuumQueueDto;
import rs.raf.demo.domain.entities.vacuum.Vacuum;
import rs.raf.demo.domain.entities.vacuum.VacuumAction;
import rs.raf.demo.domain.entities.vacuum.VacuumError;
import rs.raf.demo.domain.entities.vacuum.VacuumStatus;
import rs.raf.demo.domain.exceptions.NotFoundException;
import rs.raf.demo.domain.exceptions.VacuumException;
import rs.raf.demo.domain.mapper.VacuumMapper;
import rs.raf.demo.repositories.VacuumErrorRepository;
import rs.raf.demo.repositories.VacuumRepository;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.Date;

@Component
public class VacuumQueueListener {

    private final VacuumRepository vacuumRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final VacuumErrorRepository vacuumErrorRepository;

    @Autowired
    public VacuumQueueListener(VacuumRepository vacuumRepository, SimpMessagingTemplate simpMessagingTemplate, VacuumErrorRepository vacuumErrorRepository) {
        this.vacuumRepository = vacuumRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.vacuumErrorRepository = vacuumErrorRepository;
    }

    @RabbitListener(queues = "vacuumQueue")
    public void vacuumQueueHandler(Message message) throws IOException, ClassNotFoundException, InterruptedException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(message.getBody());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        VacuumQueueDto vacuumQueueDto = (VacuumQueueDto) objectInputStream.readObject();

        System.out.println("Queue starting: " + vacuumQueueDto.getEmail() + " - " + vacuumQueueDto.getId());

        if(vacuumQueueDto.getVacuumAction() == VacuumAction.START){
//            startStopVacuum(vacuumQueueDto.getId(), vacuumQueueDto.getEmail(), VacuumStatus.RUNNING);
            startVacuum(vacuumQueueDto.getId(), vacuumQueueDto.getEmail());
        }else if (vacuumQueueDto.getVacuumAction() == VacuumAction.STOP){
//            startStopVacuum(vacuumQueueDto.getId(), vacuumQueueDto.getEmail(), VacuumStatus.STOPPED);
            stopVacuum(vacuumQueueDto.getId(), vacuumQueueDto.getEmail());
        }else if (vacuumQueueDto.getVacuumAction() == VacuumAction.DISCHARGE){
            dischargeVacuum(vacuumQueueDto.getId(), vacuumQueueDto.getEmail());
        }

    }

//    @Transactional
//    public void startStopVacuum(Long id, String email, VacuumStatus status) throws InterruptedException {
//        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(() -> new NotFoundException("invalid vacuum id"));
//
//        Thread.sleep(15000);
//
//        try{
//            vacuum.setStatus(status);
//            vacuum.setBusy(false);
//            vacuum = vacuumRepository.saveAndFlush(vacuum);
//        }catch (ObjectOptimisticLockingFailureException e){
//            throw new VacuumException("someone has overtaken you in action");
//        }
//
//        simpMessagingTemplate.convertAndSend("/vacuum-fe/" + email, VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuum));
//    }

    @Transactional
    public void stopVacuum(Long id, String email) throws InterruptedException {
        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(() -> new NotFoundException("invalid vacuum id"));

        Thread.sleep(15000);

        try{
            vacuum.setStatus(VacuumStatus.STOPPED);
            vacuum.setBusy(false);
            vacuum.setCircles(vacuum.getCircles().intValue() + 1);
            vacuum = vacuumRepository.saveAndFlush(vacuum);

        }catch (ObjectOptimisticLockingFailureException e){
            VacuumError vacuumError = new VacuumError();
            vacuumError.setMessage("someone has overtaken you in action");
            vacuumError.setAction(VacuumAction.START);
            vacuumError.setVacuum(vacuum);

            Date date = Date.from(Instant.now());
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("someone has overtaken you in action");
        }

        simpMessagingTemplate.convertAndSend("/vacuum-fe/" + email, VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuum));

        if(vacuum.getCircles().intValue() == 3){
            dischargeVacuum(id, email);
        }
    }

    @Transactional
    public void startVacuum(Long id, String email) throws InterruptedException {
        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(() -> new NotFoundException("invalid vacuum id"));

        Thread.sleep(15000);

        try{
            vacuum.setStatus(VacuumStatus.RUNNING);
            vacuum.setBusy(false);
            vacuum = vacuumRepository.saveAndFlush(vacuum);

        }catch (ObjectOptimisticLockingFailureException e){

            VacuumError vacuumError = new VacuumError();
            vacuumError.setMessage("someone has overtaken you in action");
            vacuumError.setAction(VacuumAction.START);
            vacuumError.setVacuum(vacuum);

            Date date = Date.from(Instant.now());
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("someone has overtaken you in action");
        }

        simpMessagingTemplate.convertAndSend("/vacuum-fe/" + email, VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuum));
    }


    @Transactional
    public void dischargeVacuum(Long id, String email) throws InterruptedException {
        Vacuum vacuum = vacuumRepository.findById(id).orElseThrow(() -> new NotFoundException("invalid vacuum id"));

        try {
            Thread.sleep(15000);

            vacuum.setStatus(VacuumStatus.DISCHARGING);
            vacuum = vacuumRepository.saveAndFlush(vacuum);

            simpMessagingTemplate.convertAndSend("/vacuum-fe/" + email, VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuum));

            Thread.sleep(15000);
            vacuum.setStatus(VacuumStatus.STOPPED);
            vacuum.setBusy(false);

            vacuum.setCircles(0);
            vacuum = vacuumRepository.saveAndFlush(vacuum);
        }catch (ObjectOptimisticLockingFailureException e){
            VacuumError vacuumError = new VacuumError();
            vacuumError.setMessage("someone has overtaken you in action");
            vacuumError.setAction(VacuumAction.START);
            vacuumError.setVacuum(vacuum);

            Date date = Date.from(Instant.now());
            vacuumError.setDateError(date);

            vacuumErrorRepository.save(vacuumError);

            throw new VacuumException("someone has overtaken you in action");
        }

        simpMessagingTemplate.convertAndSend("/vacuum-fe/" + email, VacuumMapper.INSTANCE.vacuumToVacuumDto(vacuum));
    }

}
