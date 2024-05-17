package rs.raf.demo.domain.dto.vacuum;

import lombok.Data;
import rs.raf.demo.domain.entities.vacuum.VacuumAction;

import java.io.Serializable;

@Data
public class VacuumQueueDto implements Serializable {

    private Long id;
    private String email;
    private VacuumAction vacuumAction;

}
