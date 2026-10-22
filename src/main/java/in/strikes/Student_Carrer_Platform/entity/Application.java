package in.strikes.Student_Carrer_Platform.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_student_opportunity",
                        columnNames = {"student_id", "opportunity_id"}
                )
        }
)
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "student_id" , nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "opportunity_id" , nullable = false)
    private Opportunity opportunity;

    private LocalDateTime appliedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

}
