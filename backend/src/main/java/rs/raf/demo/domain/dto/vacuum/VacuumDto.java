package rs.raf.demo.domain.dto.vacuum;

import lombok.Data;
import rs.raf.demo.domain.entities.vacuum.VacuumStatus;

@Data
public class VacuumDto {

    private Long id;
    private String name;
    private VacuumStatus status;
    private Long createdDate;

}
