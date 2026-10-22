package in.strikes.Student_Carrer_Platform.service;


import in.strikes.Student_Carrer_Platform.entity.Skill;
import in.strikes.Student_Carrer_Platform.entity.Student;
import in.strikes.Student_Carrer_Platform.repository.SkillRepository;
import in.strikes.Student_Carrer_Platform.repository.StudentRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final StudentRepository studentRepository;

    public SkillService(SkillRepository skillRepository , StudentRepository studentRepository){
        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
    }

    public Skill createSkill(Skill skill){
        return skillRepository.save(skill);
    }

}
