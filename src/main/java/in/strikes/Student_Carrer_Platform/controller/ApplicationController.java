package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.entity.Application;
import in.strikes.Student_Carrer_Platform.entity.ApplicationStatus;
import in.strikes.Student_Carrer_Platform.entity.Student;
import in.strikes.Student_Carrer_Platform.service.ApplicationService;
import in.strikes.Student_Carrer_Platform.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final StudentService studentService;

    public ApplicationController(ApplicationService applicationService,
                                 StudentService studentService){
        this.applicationService = applicationService;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<?> Apply(
            @RequestParam Long student_id,
            @RequestParam Long opportunity_id,
            @AuthenticationPrincipal Jwt jwt) {

        studentService.verifyStudentOwnership(student_id , jwt.getSubject());

        Application application = applicationService.apply(student_id , opportunity_id);
        return ResponseEntity.ok(application);
    }

    @GetMapping
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {


        Application application = applicationService.getApplicationById(id);

        String email = jwt.getSubject();

        Student student = studentService.getStudentByEmail(email);

        if(!applicationService.isApplicationOwner(id, student.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to view this application");
        }

        return ResponseEntity.ok(application);
    }

    @GetMapping("/student/{student_id}")
    public ResponseEntity<?> getApplicationsByStudent(
            @PathVariable Long student_id,
            @AuthenticationPrincipal Jwt jwt) {

        studentService.verifyStudentOwnership(student_id , jwt.getSubject());

        return ResponseEntity.ok(applicationService.getApplicationsByStudent(student_id));
    }

    @GetMapping("/opportunity/{opportunity_id}")
    public List<Application> getApplicationsByOpportunity(
            @PathVariable Long opportunity_id) {
        return applicationService.getApplicationsByOpportunity(opportunity_id);
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {

        Application application =
                applicationService.updateStatus(applicationId, status);

        return ResponseEntity.ok(application);
    }



}
