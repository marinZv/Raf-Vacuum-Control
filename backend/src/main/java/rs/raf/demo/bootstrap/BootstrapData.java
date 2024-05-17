package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.demo.configuration.PasswordEncoderConfig;

import rs.raf.demo.domain.entities.user.Role;
import rs.raf.demo.domain.entities.user.RoleType;
import rs.raf.demo.domain.entities.user.User;
import rs.raf.demo.domain.entities.vacuum.Vacuum;
import rs.raf.demo.domain.entities.vacuum.VacuumAction;
//import rs.raf.demo.domain.entities.vacuum.VacuumSchedule;
import rs.raf.demo.repositories.*;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final VacuumRepository vacuumRepository;
//    private final VacuumScheduleRepository vacuumScheduleRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, RoleRepository roleRepository,  PasswordEncoderConfig passwordEncoderConfig, VacuumRepository vacuumRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.vacuumRepository = vacuumRepository;
//        this.vacuumScheduleRepository = vacuumScheduleRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        User userAdmin = new User();

        Role roleCreate = new Role();
        roleCreate.setRole(RoleType.CAN_CREATE);

        Role roleRead = new Role();
        roleRead.setRole(RoleType.CAN_READ);

        Role roleUpdate = new Role();
        roleUpdate.setRole(RoleType.CAN_UPDATE);

        Role roleDelete = new Role();
        roleDelete.setRole(RoleType.CAN_DELETE);

        Role roleCanSearchVacuums = new Role();
        roleCanSearchVacuums.setRole(RoleType.CAN_SEARCH_VACUUM);

        Role roleCanAddVacuum = new Role();
        roleCanAddVacuum.setRole(RoleType.CAN_ADD_VACUUM);

        Role roleCanRemoveVacuum = new Role();
        roleCanRemoveVacuum.setRole(RoleType.CAN_REMOVE_VACUUM);

        Role roleCanStartVacuum = new Role();
        roleCanStartVacuum.setRole(RoleType.CAN_START_VACUUM);

        Role roleCanStopVacuum = new Role();
        roleCanStopVacuum.setRole(RoleType.CAN_STOP_VACUUM);

        Role roleCanDischargeVacuum = new Role();
        roleCanDischargeVacuum.setRole(RoleType.CAN_DISCHARGE_VACUUM);

        List roles = new ArrayList();
        roles.add(roleCreate);
        roles.add(roleDelete);
        roles.add(roleUpdate);
        roles.add(roleRead);
        roles.add(roleCanAddVacuum);
        roles.add(roleCanRemoveVacuum);
        roles.add(roleCanSearchVacuums);
        roles.add(roleCanStartVacuum);
        roles.add(roleCanStopVacuum);
        roles.add(roleCanDischargeVacuum);

        Vacuum vacuum1 = new Vacuum();
        vacuum1.setName("vacuum1");
        vacuum1.setCreatedBy(userAdmin);
        vacuum1.setCreatedDate(Date.from(Instant.now()));

        Vacuum vacuum2 = new Vacuum();
        vacuum2.setName("vacuum2");
        vacuum2.setCreatedBy(userAdmin);
        vacuum2.setCreatedDate(Date.from(Instant.now()));

        List<Vacuum> vacuums = new ArrayList<>();
        vacuums.add(vacuum1);
        vacuums.add(vacuum2);

        userAdmin.setFirstName("Admin");
        userAdmin.setLastName("Admin");
        userAdmin.setEmail("admin@gmail.com");
        userAdmin.setPassword(passwordEncoderConfig.passwordEncoder().encode("admin"));
        userAdmin.setRoles(roles);
        userAdmin.setVacuums(vacuums);

        userRepository.save(userAdmin);

//        VacuumSchedule vacuumSchedule = new VacuumSchedule();
//        vacuumSchedule.setAction(VacuumAction.START);
//        vacuumSchedule.setVacuum(vacuum1);
//        vacuumSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + 2*60000)));




//        List<Role> roles1 = roleRepository.findAllByRoleIn(roleTypeList);

//        user1.setRoles(roles1);



//        userRepository.save(user1);


//        vacuumScheduleRepository.save(vacuumSchedule);


        User userAdmin1 = new User();

        Role roleCreate1 = new Role();
        roleCreate1.setRole(RoleType.CAN_CREATE);

        Role roleRead1 = new Role();
        roleRead1.setRole(RoleType.CAN_READ);

        Role roleUpdate1 = new Role();
        roleUpdate1.setRole(RoleType.CAN_UPDATE);

        Role roleDelete1 = new Role();
        roleDelete1.setRole(RoleType.CAN_DELETE);

        Role roleCanSearchVacuums1 = new Role();
        roleCanSearchVacuums1.setRole(RoleType.CAN_SEARCH_VACUUM);

        Role roleCanAddVacuum1 = new Role();
        roleCanAddVacuum1.setRole(RoleType.CAN_ADD_VACUUM);

        Role roleCanRemoveVacuum1 = new Role();
        roleCanRemoveVacuum1.setRole(RoleType.CAN_REMOVE_VACUUM);

        Role roleCanStartVacuum1 = new Role();
        roleCanStartVacuum1.setRole(RoleType.CAN_START_VACUUM);

        Role roleCanStopVacuum1 = new Role();
        roleCanStopVacuum1.setRole(RoleType.CAN_STOP_VACUUM);

        Role roleCanDischargeVacuum1 = new Role();
        roleCanDischargeVacuum1.setRole(RoleType.CAN_DISCHARGE_VACUUM);

        List roles1 = new ArrayList();
        roles1.add(roleCreate1);
        roles1.add(roleDelete1);
        roles1.add(roleUpdate1);
        roles1.add(roleRead1);
        roles1.add(roleCanAddVacuum1);
        roles1.add(roleCanRemoveVacuum1);
        roles1.add(roleCanSearchVacuums1);
        roles1.add(roleCanStartVacuum1);
        roles1.add(roleCanStopVacuum1);
        roles1.add(roleCanDischargeVacuum1);

        Vacuum vacuum3 = new Vacuum();
        vacuum3.setName("vacuum3");
        vacuum3.setCreatedBy(userAdmin1);
        vacuum3.setCreatedDate(Date.from(Instant.now()));

        Vacuum vacuum4 = new Vacuum();
        vacuum4.setName("vacuum4");
        vacuum4.setCreatedBy(userAdmin1);
        vacuum4.setCreatedDate(Date.from(Instant.now()));

        List<Vacuum> vacuums1 = new ArrayList<>();
        vacuums1.add(vacuum3);
        vacuums1.add(vacuum4);

        userAdmin1.setFirstName("Admin1");
        userAdmin1.setLastName("Admin1");
        userAdmin1.setEmail("admin1@gmail.com");
        userAdmin1.setPassword(passwordEncoderConfig.passwordEncoder().encode("admin1"));
        userAdmin1.setRoles(roles1);
        userAdmin1.setVacuums(vacuums1);

        userRepository.save(userAdmin1);


        System.out.println("DATA LOADED!");

    }


}
