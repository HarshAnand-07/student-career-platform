package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.entity.Application;
import in.strikes.Student_Carrer_Platform.entity.ApplicationStatus;
import in.strikes.Student_Carrer_Platform.entity.Opportunity;
import in.strikes.Student_Carrer_Platform.entity.Student;
import in.strikes.Student_Carrer_Platform.exception.DuplicateApplicationException;
import in.strikes.Student_Carrer_Platform.exception.InvalidStatusTransitionException;
import in.strikes.Student_Carrer_Platform.exception.ResourceNotFoundException;
import in.strikes.Student_Carrer_Platform.repository.ApplicationRepository;
import in.strikes.Student_Carrer_Platform.repository.OpportunityRepository;
import in.strikes.Student_Carrer_Platform.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OpportunityRepository opportunityRepository;
    private final StudentRepository studentRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              StudentRepository studentRepository,
                              OpportunityRepository opportunityRepository){
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Transactional
    public Application apply(Long student_id,
                             Long opportunity_id) {
        Student student = studentRepository.findById(student_id).orElse(null);
        Opportunity opportunity = opportunityRepository.findById(opportunity_id).orElse(null);

        if(student == null){
            throw  new ResourceNotFoundException("Student_id is invalid");
        }

        if(opportunity == null){
            throw  new ResourceNotFoundException("Opportunity_id is invalid");
        }


        if(applicationRepository.existsByStudentIdAndOpportunityId(student_id , opportunity_id)){
            throw new DuplicateApplicationException("Student has already applied to this opportunity");
        }
        Application application = new Application();
        application.setStudent(student);
        application.setOpportunity(opportunity);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);

        return applicationRepository.save(application);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Application Not Found"));
    }

    public boolean isApplicationOwner(Long applicationId, Long studentId) {
        Application application = applicationRepository
                .findById(applicationId)
                .orElse(null);

        return application != null
                && application.getStudent().getId().equals(studentId);
    }

    public List<Application> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<Application> getApplicationsByOpportunity(Long opportunityId) {
        return applicationRepository.findByOpportunityId(opportunityId);
    }

    public Application updateStatus(Long application_id , ApplicationStatus status){
        Application application = applicationRepository.findById(application_id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Application Not Found")
                );

        ApplicationStatus currentStatus = application.getStatus();

        boolean isAllowed = switch (currentStatus){
            case APPLIED ->
                status == ApplicationStatus.SHORTLISTED ||
                        status == ApplicationStatus.REJECTED;

            case SHORTLISTED ->
                status == ApplicationStatus.INTERVIEW ||
                        status == ApplicationStatus.REJECTED;

            case INTERVIEW ->
                status == ApplicationStatus.SELECTED ||
                        status == ApplicationStatus.REJECTED;

            case SELECTED , REJECTED -> false;
        };

        if(!isAllowed){
            throw  new InvalidStatusTransitionException(
                    "Cannot change application status from "
                            + currentStatus + " to " + status
            );
        }

        application.setStatus(status);

        return applicationRepository.save(application);
    }

}
