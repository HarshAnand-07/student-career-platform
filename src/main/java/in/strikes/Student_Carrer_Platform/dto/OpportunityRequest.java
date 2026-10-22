package in.strikes.Student_Carrer_Platform.dto;

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

public class OpportunityRequest {

    @NotBlank(message = "Opportunity title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Organization is required")
    @Size(max = 150, message = "Organization cannot exceed 150 characters")
    private String organization;

    @NotBlank(message = "Opportunity type is required")
    @Size(max = 50, message = "Type cannot exceed 50 characters")
    private String type;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 150, message = "Location cannot exceed 150 characters")
    private String location;

    @Pattern(
            regexp = "^https?://.+$",
            message = "Please provide a valid application URL starting with http:// or https://"
    )
    @Size(max = 2048, message = "Application URL cannot exceed 2048 characters")
    private String applicationUrl;

}
