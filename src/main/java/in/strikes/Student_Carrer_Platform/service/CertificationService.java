package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.entity.Certification;
import in.strikes.Student_Carrer_Platform.repository.CertificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationService(CertificationRepository certificationRepository){
        this.certificationRepository = certificationRepository;
    }

    public Certification createCertification(Certification certification){
        return certificationRepository.save(certification);
    }

    public List<Certification> getAllCertifications() {
        return certificationRepository.findAll();
    }

}
