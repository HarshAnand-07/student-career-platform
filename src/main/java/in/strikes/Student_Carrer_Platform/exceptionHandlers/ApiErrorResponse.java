package in.strikes.Student_Carrer_Platform.exceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;

}
