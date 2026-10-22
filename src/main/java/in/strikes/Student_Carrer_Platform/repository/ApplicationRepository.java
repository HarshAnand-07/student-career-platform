package in.strikes.Student_Carrer_Platform.repository;


import in.strikes.Student_Carrer_Platform.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application , Long> {

    boolean existsByStudentIdAndOpportunityId(
            Long student_id,
            Long opportunity_id
    );

    List<Application> findByStudentId(Long studentId);

    List<Application> findByOpportunityId(Long opportunityId);

    void deleteByStudentId(Long studentId);

}
