package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import rs.raf.demo.domain.entities.user.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    Optional<User> findByEmail(String email);

}
