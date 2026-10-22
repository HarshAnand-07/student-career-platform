package in.strikes.Student_Carrer_Platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "requiredSkills")
    @JsonIgnore
    private Set<Opportunity> opportunities = new HashSet<>();

}
