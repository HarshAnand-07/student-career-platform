package in.strikes.Student_Carrer_Platform.repository;

import in.strikes.Student_Carrer_Platform.entity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity , Long> {

     List<Opportunity> findByType(String type);
     Page<Opportunity> findByType(String type, Pageable pageable);
     List<Opportunity> findByLocation(String location);
     Page<Opportunity> findByLocation(String location, Pageable pageable);

    List<Opportunity> findByTypeAndLocation(
            String location,
            String type
    );

    Page<Opportunity> findByTypeAndLocation(
            String type,
            String location,
            Pageable pageable
    );

    Page<Opportunity>
    findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrOrganizationContainingIgnoreCase(
            String title,
            String description,
            String organization,
            Pageable pageable

    );

    @Query("""
    SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END
    FROM Opportunity o
    WHERE LOWER(o.title) = LOWER(:title)
      AND LOWER(o.organization) = LOWER(:organization)
      AND LOWER(o.type) = LOWER(:type)
      AND LOWER(COALESCE(o.location, '')) =
          LOWER(COALESCE(:location, ''))
      AND LOWER(COALESCE(o.applicationUrl, '')) =
          LOWER(COALESCE(:applicationUrl, ''))
    """)
    boolean existsDuplicate(
            String title,
            String organization,
            String type,
            String location,
            String applicationUrl
    );

    @Query("""
    SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END
    FROM Opportunity o
    WHERE o.id <> :id
      AND LOWER(o.title) = LOWER(:title)
      AND LOWER(o.organization) = LOWER(:organization)
      AND LOWER(o.type) = LOWER(:type)
      AND LOWER(COALESCE(o.location, '')) =
          LOWER(COALESCE(:location, ''))
      AND LOWER(COALESCE(o.applicationUrl, '')) =
          LOWER(COALESCE(:applicationUrl, ''))
    """)
    boolean existsDuplicateExcludingId(
            Long id,
            String title,
            String organization,
            String type,
            String location,
            String applicationUrl
    );

}
