package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.dto.StudentPatchRequest;
import in.strikes.Student_Carrer_Platform.entity.*;
import in.strikes.Student_Carrer_Platform.exception.DuplicateStudentEmailException;
import in.strikes.Student_Carrer_Platform.exception.ResourceNotFoundException;
import in.strikes.Student_Carrer_Platform.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final CertificationRepository certificationRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public StudentService(StudentRepository studentRepository ,
                          SkillRepository skillRepository ,
                          ProjectRepository projectRepository ,
                          CertificationRepository certificationRepository,
                          ApplicationRepository applicationRepository,
                          UserRepository userRepository){
        this.studentRepository = studentRepository;
        this.skillRepository = skillRepository;
        this.projectRepository = projectRepository;
        this.certificationRepository = certificationRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public Student createStudent(Student student){

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new DuplicateStudentEmailException(
                    "A student with this email already exists");
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudent(){
        return studentRepository.findAll();
    }

    public Student getStudentByID(Long id){
        return studentRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Student Not Found"));
    }

    @Transactional
    public Student updateStudent(Long id , Student student){
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Student Not Found"));


        existingStudent.setCollege(student.getCollege());
        existingStudent.setName(student.getName());
        existingStudent.setCourse(student.getCourse());
        existingStudent.setGraduationYear(student.getGraduationYear());
        existingStudent.setResumeUrl(student.getResumeUrl());

        Student savedStudent = studentRepository.save(existingStudent);
        syncUserName(savedStudent);
        return savedStudent;

    }

    @Transactional
    public Student patchStudent(Long id, StudentPatchRequest studentPatchRequest) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student Not Found"));

        if (studentPatchRequest.getName() != null) {
            existingStudent.setName(studentPatchRequest.getName());
        }

        if (studentPatchRequest.getCollege() != null) {
            existingStudent.setCollege(studentPatchRequest.getCollege());
        }

        if (studentPatchRequest.getCourse() != null) {
            existingStudent.setCourse(studentPatchRequest.getCourse());
        }

        if (studentPatchRequest.getGraduationYear() != null) {
            existingStudent.setGraduationYear(studentPatchRequest.getGraduationYear());
        }

        if (studentPatchRequest.getResumeUrl() != null) {
            existingStudent.setResumeUrl(studentPatchRequest.getResumeUrl());
        }

        Student savedStudent = studentRepository.save(existingStudent);

        if (studentPatchRequest.getName() != null) {
            syncUserName(savedStudent);
        }

        return savedStudent;
    }

    private void syncUserName(Student student) {
        User user = userRepository.findByEmail(student.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found"));

        user.setName(student.getName());
        userRepository.save(user);
    }

    @Transactional
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found")
                );

        applicationRepository.deleteByStudentId(id);

        studentRepository.delete(student);
    }

    public Student addSkillToStudent(Long student_id , Long skill_id){
        Skill skill = skillRepository.findById(skill_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill Not Found"));

        Student student = studentRepository.findById(student_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student Not Found"));

        student.getSkills().add(skill);

        return studentRepository.save(student);
    }

    public Student addProjectToStudent(Long student_id , Long project_id){
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project Not Found"));

        Student student = studentRepository.findById(student_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student Not Found"));


        if (project.getStudent() != null
                && !project.getStudent().getId().equals(student_id)) {
            throw new AccessDeniedException(
                    "This project belongs to another student"
            );
        }

        project.setStudent(student);

        if (student.getProjects().stream()
                .noneMatch(p -> p.getId().equals(project_id))) {
            student.getProjects().add(project);
        }

        projectRepository.save(project);


        return student;
    }

    public Student addCertificationToStudent(Long studentId, Long certificationId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student Not Found"));

        Certification certification =
                certificationRepository.findById(certificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Certification Not Found"));


        student.getCertifications().add(certification);

        return studentRepository.save(student);
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));
    }

    public void verifyStudentOwnership(Long studentId, String email) {

        Student loggedInStudent = getStudentByEmail(email);

        if (!loggedInStudent.getId().equals(studentId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to modify another student's profile"
            );
        }
    }

    public Student createStudentProfile(String name, String email) {
        Student student = new Student();

        student.setName(name);
        student.setEmail(email);

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteOwnAccount(String email){
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Student Not Found")
                );

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("User Not Found")
                );

        deleteStudent(student.getId());

        userRepository.delete(user);

    }

}
