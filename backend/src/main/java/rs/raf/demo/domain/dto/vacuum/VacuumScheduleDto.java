package rs.raf.demo.domain.dto.vacuum;

import lombok.Data;
import rs.raf.demo.domain.entities.vacuum.VacuumAction;
import rs.raf.demo.domain.entities.vacuum.VacuumStatus;

import java.io.Serializable;
import java.util.Date;

@Data
public class VacuumScheduleDto implements Serializable {

    private Long id;
    private Long scheduleDate;
    private VacuumAction action;

}
