package in.strikes.Student_Carrer_Platform.service;

import in.strikes.Student_Carrer_Platform.dto.LoginRequest;
import in.strikes.Student_Carrer_Platform.dto.RegisterRequest;
import in.strikes.Student_Carrer_Platform.entity.User;
import in.strikes.Student_Carrer_Platform.exception.EmailAlreadyExistsException;
import in.strikes.Student_Carrer_Platform.exception.InvalidCredentialsException;
import in.strikes.Student_Carrer_Platform.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StudentService studentService;

    public AuthService(UserRepository userRepository ,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       StudentService studentService){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.studentService = studentService;
    }

    @Transactional
    public void Register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("STUDENT");

        userRepository.save(user);

        studentService.createStudentProfile(user.getName(), user.getEmail());

    }

    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new InvalidCredentialsException("Invalid Email or Password"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())){
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

    }

}
