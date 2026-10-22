package in.strikes.Student_Carrer_Platform.repository;

import in.strikes.Student_Carrer_Platform.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project , Long> {

    List<Project> findByStudentEmail(String email);

}
