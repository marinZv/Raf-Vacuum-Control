//package rs.raf.demo.domain.entities.vacuum;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Data
//@Entity
//public class VacuumSchedule {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private VacuumAction action;
//
//    @ManyToOne
//    private Vacuum vacuum;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date scheduleDate;
//
//}
