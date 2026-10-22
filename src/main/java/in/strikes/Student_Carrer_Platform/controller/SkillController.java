package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.entity.Skill;
import in.strikes.Student_Carrer_Platform.service.SkillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public Skill createSkill(@RequestBody Skill skill) {
        return skillService.createSkill(skill);
    }
}
