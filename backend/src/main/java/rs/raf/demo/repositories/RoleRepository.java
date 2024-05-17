package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import rs.raf.demo.domain.entities.user.Role;
import rs.raf.demo.domain.entities.user.RoleType;

import java.util.List;

public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {

    List<Role> findAllByRoleIn(List<RoleType> roles);

}
