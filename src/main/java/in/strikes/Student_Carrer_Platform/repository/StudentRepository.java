package in.strikes.Student_Carrer_Platform.repository;

import in.strikes.Student_Carrer_Platform.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student , Long> {

    Optional<Student> findByEmail(String Email);

}
