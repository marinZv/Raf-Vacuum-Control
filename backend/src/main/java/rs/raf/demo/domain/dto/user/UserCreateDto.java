package rs.raf.demo.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> userRoles;
}
