package in.strikes.Student_Carrer_Platform.controller;

import in.strikes.Student_Carrer_Platform.dto.LoginRequest;
import in.strikes.Student_Carrer_Platform.dto.RegisterRequest;
import in.strikes.Student_Carrer_Platform.entity.User;
import in.strikes.Student_Carrer_Platform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
            ){
        authService.Register(request);

        return ResponseEntity.ok("User Registered Succesfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request
            ){
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

}
