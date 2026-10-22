package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.dto.StudentPatchRequest;
import in.strikes.Student_Carrer_Platform.entity.Student;
import in.strikes.Student_Carrer_Platform.service.SkillService;
import in.strikes.Student_Carrer_Platform.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student){
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudent(){
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentByID(@PathVariable Long id,
                                            @AuthenticationPrincipal Jwt jwt){

        Student student = studentService.getStudentByID(id);
        boolean isAdmin = "ADMIN".equals(jwt.getClaimAsString("role"));

        String email = jwt.getSubject();

        if(!isAdmin && !student.getEmail().equalsIgnoreCase(email)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to view another student's profile");
        }

        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
           @Valid @RequestBody Student student,
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        boolean isAdmin = "ADMIN".equals(jwt.getClaimAsString("role"));

        if (!isAdmin) {
            Student loggedInStudent = studentService.getStudentByEmail(email);

            if (!loggedInStudent.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        Student updatedStudent = studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal Jwt jwt){

        String email = jwt.getSubject();

        studentService.deleteOwnAccount(email);

        return ResponseEntity.ok("Your account has been deleted successfully");
    }

    @PutMapping("/{student_id}/skills/{skill_id}")
    public Student addSkillToStudent(@PathVariable Long student_id,
                                     @PathVariable Long skill_id,
                                     @AuthenticationPrincipal Jwt jwt){

        String email = jwt.getSubject();
        studentService.verifyStudentOwnership(student_id , email);
        return studentService.addSkillToStudent(student_id , skill_id);

    }

    @PutMapping("/{student_id}/projects/{project_id}")
    public Student addProjectToStudent(@PathVariable Long student_id,
                                       @PathVariable Long project_id,
                                       @AuthenticationPrincipal Jwt jwt){

        String email = jwt.getSubject();
        studentService.verifyStudentOwnership(student_id , email);

        return studentService.addProjectToStudent(student_id , project_id);
    }

    @PutMapping("/{student_id}/certifications/{certification_id}")
    public Student addCertificationToStudent(@PathVariable Long student_id,
                                             @PathVariable Long certification_id,
                                             @AuthenticationPrincipal Jwt jwt){

        String email = jwt.getSubject();
        studentService.verifyStudentOwnership(student_id , email);

        return studentService.addCertificationToStudent(student_id , certification_id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentPatchRequest studentPatchRequest,
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        boolean isAdmin = "ADMIN".equals(jwt.getClaimAsString("role"));

        if (!isAdmin) {
            Student loggedInStudent = studentService.getStudentByEmail(email);

            if (!loggedInStudent.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not allowed to update another student's profile");
            }
        }

        Student updatedStudent = studentService.patchStudent(id, studentPatchRequest);

        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getStudentProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Student student = studentService.getStudentByID(id);

        String email = jwt.getSubject();
        boolean isAdmin = "ADMIN".equals(jwt.getClaimAsString("role"));

        if (!isAdmin && !student.getEmail().equalsIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to view another student's profile");
        }

        return ResponseEntity.ok(student);
    }

}
