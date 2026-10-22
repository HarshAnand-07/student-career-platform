package in.strikes.Student_Carrer_Platform.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Certification name is required")
    @Size(max = 150, message = "Certification name cannot exceed 150 characters")
    private String name;

    @Size(max = 150, message = "Issuing organization cannot exceed 150 characters")
    private String issuingOrganization;

    private String issueDate;

    @Size(max = 2048, message = "Credential URL cannot exceed 2048 characters")
    private String credentialUrl;

}
