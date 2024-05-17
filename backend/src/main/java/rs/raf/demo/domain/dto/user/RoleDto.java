package rs.raf.demo.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.demo.domain.entities.user.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    RoleType role;
}
