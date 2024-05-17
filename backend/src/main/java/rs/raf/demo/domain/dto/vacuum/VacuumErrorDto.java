package rs.raf.demo.domain.dto.vacuum;

import lombok.Data;
import rs.raf.demo.domain.entities.vacuum.VacuumAction;

import java.util.Date;

@Data
public class VacuumErrorDto {

    private Long id;
    private String message;
    private VacuumAction action;
    private VacuumDto vacuum;
    private Long dateError;


}
