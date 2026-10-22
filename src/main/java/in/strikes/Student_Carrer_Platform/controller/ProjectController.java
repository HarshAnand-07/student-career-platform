package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.entity.Project;
import in.strikes.Student_Carrer_Platform.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@Valid @RequestBody Project project,
                                 @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();

        return projectService.createProjectForStudent(project , email);
    }

    @GetMapping
    public List<Project> getMyProjects(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        String role = jwt.getClaimAsString("role");

        if("ADMIN".equals(role)){
            return projectService.getAllProjects();
        }

        return projectService.getProjectsByStudentEmail(email);
    }

}
