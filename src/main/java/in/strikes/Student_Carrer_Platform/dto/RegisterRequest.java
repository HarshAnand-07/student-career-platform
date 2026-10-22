package in.strikes.Student_Carrer_Platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8 , message = "Password must be atleast 8 characters")
    private String password;

}
