package in.strikes.Student_Carrer_Platform.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Column(unique = true , length = 150)
    private String email;

    @Size(max = 150, message = "College name cannot exceed 150 characters")
    private String college;

    @Size(max = 100, message = "Course cannot exceed 100 characters")
    private String course;

    @Min(value = 2020, message = "Graduation year is invalid")
    @Max(value = 2100, message = "Graduation year is invalid")
    private Integer graduationYear;

    @Pattern(
            regexp = "^https?://.+$",
            message = "Resume URL must start with http:// or https://"
    )
    @Size(max = 2048, message = "Resume URL cannot exceed 2048 characters")
    private String resumeUrl;

    @ManyToMany
    @JoinTable(
            name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "Skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<Project> projects = new ArrayList<>();


    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "student_id")
    private List<Certification> certifications = new ArrayList<>();

}
