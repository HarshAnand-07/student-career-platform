package in.strikes.Student_Carrer_Platform.repository;

import in.strikes.Student_Carrer_Platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String Email);

    boolean existsByEmail(String Email);

}
