package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.entity.Certification;
import in.strikes.Student_Carrer_Platform.service.CertificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certification")
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService){
        this.certificationService = certificationService;
    }

    @PostMapping
    public Certification createCertification(
            @RequestBody Certification certification) {

        return certificationService.createCertification(certification);
    }

    @GetMapping
    public List<Certification> getAllCertifications() {
        return certificationService.getAllCertifications();
    }

}
