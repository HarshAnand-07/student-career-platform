package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.dto.OpportunityRequest;
import in.strikes.Student_Carrer_Platform.dto.OpportunityResponse;
import in.strikes.Student_Carrer_Platform.entity.Opportunity;
import in.strikes.Student_Carrer_Platform.service.OpportunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.yaml.snakeyaml.tokens.Token.ID.Value;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService){
        this.opportunityService = opportunityService;
    }

    @Operation(
            summary = "Create a new opportunity",
            description = "Creates a new opportunity using the provided request details. Requires ADMIN authorization."
    )
    @PostMapping
    public OpportunityResponse createOpportunity(
           @Valid @RequestBody OpportunityRequest request) {

        Opportunity createdOpportunity  = opportunityService.createOpportunity(request);

        return OpportunityResponse.fromEntity(createdOpportunity);
    }

    @Operation(
            summary = "Update an opportunity",
            description = "Updates an existing opportunity using its ID and the provided request details. Requires ADMIN authorization."
    )
    @PutMapping("/{id}")
    public OpportunityResponse updateOpportunity(
            @PathVariable Long id,
            @Valid @RequestBody OpportunityRequest request) {

        Opportunity updatedOpportunity =
                opportunityService.updateOpportunity(id, request);

        return OpportunityResponse.fromEntity(updatedOpportunity);
    }

    @Operation(
            summary = "Add a skill to an opportunity",
            description = "Associates an existing skill with an opportunity using their respective IDs. Requires ADMIN authorization."
    )
    @PutMapping("/{opportunity_id}/skills/{skill_id}")
    public OpportunityResponse addSkillToOpportunity(@PathVariable Long opportunity_id ,
                                             @PathVariable Long skill_id){
        Opportunity updatedOpportunity = opportunityService.addSkillToOpportunity(opportunity_id,
                skill_id);

        return OpportunityResponse.fromEntity(updatedOpportunity);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opportunities retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters"),
            @ApiResponse(responseCode = "401", description = "Authentication required or invalid token"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @Operation(
            summary = "Get all opportunities",
            description = "Returns a paginated list of opportunities with sorting options."
    )
    @GetMapping
    public Page<OpportunityResponse> getAllOpportunities(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of opportunities per page (maximum 100)",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int size,

            @Parameter(
                    description = "Field used to sort opportunities: title, type, or location",
                    example = "title"
            )
            @RequestParam(defaultValue = "title") String sortBy,

            @Parameter(
                    description = "Sort direction: asc for ascending or desc for descending",
                    example = "asc"
            )
            @RequestParam(defaultValue = "asc") String direction
    ) {

        if (!sortBy.equals("title")
                && !sortBy.equals("type")
                && !sortBy.equals("location")) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed: title, type, location");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Invalid direction. Allowed: asc, desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return opportunityService.getAllOpportunities(pageable);
    }

    @Operation(
            summary = "Get opportunity by ID",
            description = "Returns the details of a specific opportunity using its ID."
    )
    @GetMapping("/{id}")
    public OpportunityResponse getOpportunityById(@PathVariable Long id) {
        return opportunityService.getOpportunityById(id);
    }

    @Operation(
            summary = "Search opportunities by type",
            description = "Returns a paginated list of opportunities matching the specified type."
    )
    @GetMapping("/search/type")
    public Page<OpportunityResponse> getOpportunitiesByType(
            @RequestParam String type,
            @PageableDefault(size = 10, sort = "title",
                    direction = Sort.Direction.ASC) Pageable pageable) {

        return opportunityService.getOpportunitiesByType(type, pageable);
    }

    @Operation(
            summary = "Search opportunities by location",
            description = "Returns a paginated list of opportunities matching the specified location."
    )
    @GetMapping("/search/location")
    public Page<OpportunityResponse> getOpportunitiesByLocation(
            @RequestParam String location,
            @PageableDefault(size = 10, sort = "title",
                    direction = Sort.Direction.ASC) Pageable pageable) {

        return opportunityService.getOpportunitiesByLocation(location, pageable);
    }

    @Operation(
            summary = "Filter opportunities by location and type",
            description = "Returns paginated opportunities matching both the specified location and type."
    )
    @GetMapping("/search/filter")
    public Page<OpportunityResponse> getOpportunitiesByLocationandType(
            @RequestParam String location,
            @RequestParam String type,
            @PageableDefault(size = 10 , sort = "title",
            direction = Sort.Direction.ASC) Pageable pageable) {

        return opportunityService.getOpportunitiesByLocationandType(
                location, type, pageable);
    }

    @Operation(
            summary = "Search opportunities by keyword",
            description = "Returns paginated opportunities matching the provided keyword."
    )
    @GetMapping("/search/keyword")
    public Page<OpportunityResponse> getOpportunities(@RequestParam String keyword,
                                              @PageableDefault(size = 5,sort = "title",
                                              direction = Sort.Direction.ASC)Pageable pageable){
        return opportunityService.getOpportunities(keyword , pageable);
    }

}
