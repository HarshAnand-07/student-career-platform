package in.strikes.Student_Carrer_Platform.exception;

public class DuplicateStudentEmailException extends RuntimeException{

    public DuplicateStudentEmailException(String message){
        super(message);
    }

}
