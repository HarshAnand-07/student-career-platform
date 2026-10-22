package in.strikes.Student_Carrer_Platform;

import in.strikes.Student_Carrer_Platform.entity.User;
import in.strikes.Student_Carrer_Platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_NAME:Administrator}")
    private String adminName;

    public AdminBootstrap(UserRepository userRepository ,
                          PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(adminEmail.isBlank() && adminPassword.isBlank()){
            return;
        }

        if(adminEmail.isBlank() || adminPassword.isBlank()){
            throw new IllegalStateException
                    (
                          "Both ADMIN_EMAIL and ADMIN_PASSWORD must be configured"
                    );
        }

        var existingUser = userRepository.findByEmail(adminEmail);

        if(existingUser.isPresent()){
            if(!"ADMIN".equals(existingUser.get().getRole())){
                throw new IllegalStateException(
                        "Configured admin email already belongs to a non-admin user"
                );
            }
            return;
        }

        User admin = new User();

        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setRole("ADMIN");
        admin.setPassword(passwordEncoder.encode(adminPassword));

        userRepository.save(admin);
    }
}
