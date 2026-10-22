package in.strikes.Student_Carrer_Platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Project description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Pattern(
            regexp = "^https://github\\.com/[^/\\s]+/[^/\\s]+/?$",
            message = "Please provide a valid GitHub repository URL"
    )
    @Size(max = 2048, message = "GitHub URL cannot exceed 2048 characters")
    private String githubUrl;


    @NotBlank(message = "Tech stack is required")
    @Size(max = 500, message = "Tech stack cannot exceed 500 characters")
    private String techStack;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
