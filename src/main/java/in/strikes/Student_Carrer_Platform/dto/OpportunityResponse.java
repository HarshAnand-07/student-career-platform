package in.strikes.Student_Carrer_Platform.dto;

import in.strikes.Student_Carrer_Platform.entity.Opportunity;
import in.strikes.Student_Carrer_Platform.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityResponse {

    private Long id;
    private String title;
    private String organization;
    private String type;
    private String description;
    private String location;
    private String applicationUrl;
    private Set<String> requiredSkills;

    public static OpportunityResponse fromEntity(Opportunity opportunity) {
        OpportunityResponse response = new OpportunityResponse();

        response.setId(opportunity.getId());
        response.setTitle(opportunity.getTitle());
        response.setOrganization(opportunity.getOrganization());
        response.setType(opportunity.getType());
        response.setDescription(opportunity.getDescription());
        response.setLocation(opportunity.getLocation());
        response.setApplicationUrl(opportunity.getApplicationUrl());

        response.setRequiredSkills(
                opportunity.getRequiredSkills()
                        .stream()
                        .map(Skill::getName)
                        .collect(Collectors.toSet())
        );

        return response;
    }

}
