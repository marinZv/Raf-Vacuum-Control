package rs.raf.demo.domain.entities.vacuum;

import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.demo.domain.entities.user.User;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Vacuum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VacuumStatus status = VacuumStatus.STOPPED;

    private String name;
    private boolean active = true;
    private boolean busy = false;

    @ManyToOne
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Integer circles = 0;

    @Column
    @Version
    private Integer version = 0;
}
