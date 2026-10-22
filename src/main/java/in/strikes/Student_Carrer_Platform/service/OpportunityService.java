package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.dto.OpportunityRequest;
import in.strikes.Student_Carrer_Platform.dto.OpportunityResponse;
import in.strikes.Student_Carrer_Platform.entity.Opportunity;
import in.strikes.Student_Carrer_Platform.entity.Skill;
import in.strikes.Student_Carrer_Platform.exception.DuplicateOpportunityException;
import in.strikes.Student_Carrer_Platform.exception.ResourceNotFoundException;
import in.strikes.Student_Carrer_Platform.repository.OpportunityRepository;
import in.strikes.Student_Carrer_Platform.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final SkillRepository skillRepository;

    public OpportunityService(OpportunityRepository opportunityRepository ,
                              SkillRepository skillRepository){
        this.opportunityRepository = opportunityRepository;
        this.skillRepository = skillRepository;
    }

    public Opportunity createOpportunity(OpportunityRequest request){

        boolean duplicateExists =
                opportunityRepository
                        .existsDuplicate(
                                request.getTitle(),
                                request.getOrganization(),
                                request.getType(),
                                request.getLocation(),
                                request.getApplicationUrl()
                        );

        if (duplicateExists) {
            throw new DuplicateOpportunityException(
                    "This opportunity already exists");
        }

        Opportunity opportunity = new Opportunity();

        opportunity.setTitle(request.getTitle());
        opportunity.setApplicationUrl(request.getApplicationUrl());
        opportunity.setDescription(request.getDescription());
        opportunity.setType(request.getType());
        opportunity.setLocation(request.getLocation());
        opportunity.setOrganization(request.getOrganization());


        return opportunityRepository.save(opportunity);
    }

    public Opportunity updateOpportunity(Long id, OpportunityRequest request) {

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Opportunity not found"));

        boolean duplicateExists =
                opportunityRepository.existsDuplicateExcludingId(
                        id,
                        request.getTitle(),
                        request.getOrganization(),
                        request.getType(),
                        request.getLocation(),
                        request.getApplicationUrl()
                );

        if (duplicateExists) {
            throw new DuplicateOpportunityException(
                    "This opportunity already exists"
            );
        }

        opportunity.setTitle(request.getTitle());
        opportunity.setOrganization(request.getOrganization());
        opportunity.setType(request.getType());
        opportunity.setDescription(request.getDescription());
        opportunity.setLocation(request.getLocation());
        opportunity.setApplicationUrl(request.getApplicationUrl());

        return opportunityRepository.save(opportunity);
    }

    public Opportunity addSkillToOpportunity(Long opportunity_id ,
                                             Long skill_id){
        Skill skill = skillRepository.findById(skill_id).orElse(null);
        Opportunity opportunity = opportunityRepository.findById(opportunity_id).orElse(null);

        if(skill == null || opportunity == null) return null;

        opportunity.getRequiredSkills().add(skill);

        return opportunityRepository.save(opportunity);

    }

    public OpportunityResponse getOpportunityById(Long id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Opportunity not found"));

        return OpportunityResponse.fromEntity(opportunity);
    }


    public Page<OpportunityResponse> getOpportunitiesByType(
            String type, Pageable pageable) {

        validatePageable(pageable);

        return opportunityRepository.findByType(type, pageable)
                .map(OpportunityResponse::fromEntity);
    }

    public Page<OpportunityResponse> getOpportunitiesByLocation(
            String location, Pageable pageable) {

        validatePageable(pageable);

        return opportunityRepository.findByLocation(location, pageable)
                .map(OpportunityResponse::fromEntity);
    }

    public Page<OpportunityResponse> getOpportunitiesByLocationandType(
            String location, String type, Pageable pageable) {

        validatePageable(pageable);

        return opportunityRepository.findByTypeAndLocation(
                type, location, pageable)
                .map(OpportunityResponse::fromEntity);
    }

    public Page<OpportunityResponse> getOpportunities(String keyword , Pageable pageable){

        validatePageable(pageable);

        return opportunityRepository.
                findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrOrganizationContainingIgnoreCase
                (keyword ,keyword ,keyword , pageable)
//                .map(OpportunityResponse :: fromEntity);
                .map(opportunity -> OpportunityResponse.fromEntity(opportunity));
    }

    public Page<OpportunityResponse> getAllOpportunities(Pageable pageable) {
        return opportunityRepository.findAll(pageable)
//                .map(opportunity -> OpportunityResponse.fromEntity(opportunity));
                .map(OpportunityResponse::fromEntity);
    }


    private void validatePageable(Pageable pageable) {

        if (pageable.getPageSize() < 1
                || pageable.getPageSize() > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        for (Sort.Order order : pageable.getSort()) {
            String field = order.getProperty();

            if (!field.equals("title")
                    && !field.equals("type")
                    && !field.equals("location")) {
                throw new IllegalArgumentException(
                        "Invalid sort field. Allowed: title, type, location"
                );
            }
        }
    }
}
