package in.strikes.Student_Carrer_Platform.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPatchRequest {

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @NotBlank(message = "Name cannot be blank")
    private String name;

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

}