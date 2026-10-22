package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.entity.Project;
import in.strikes.Student_Carrer_Platform.entity.Student;
import in.strikes.Student_Carrer_Platform.exception.ResourceNotFoundException;
import in.strikes.Student_Carrer_Platform.repository.ProjectRepository;
import in.strikes.Student_Carrer_Platform.repository.StudentRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    public ProjectService(ProjectRepository projectRepository,
                          StudentRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    public Project createProjectForStudent(Project project , String email){

        Student student = studentRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Student Not Found")
        );

        project.setId(null);
        project.setStudent(student);

        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByStudentEmail(String email) {
        return projectRepository.findByStudentEmail(email);
    }

}
