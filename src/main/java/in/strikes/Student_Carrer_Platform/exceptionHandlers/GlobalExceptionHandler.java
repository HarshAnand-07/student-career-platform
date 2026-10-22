package in.strikes.Student_Carrer_Platform.exceptionHandlers;

import in.strikes.Student_Carrer_Platform.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .filter(msg -> msg != null && !msg.isBlank())
                .findFirst()
                .orElse("Invalid request data");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateApplication(
            DuplicateApplicationException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "The request conflicts with existing data",
                request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value provided for parameter: " + ex.getName(),
                request);
    }

    @ExceptionHandler(DuplicateStudentEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateStudentEmail(
            DuplicateStudentEmailException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStatusTransition(
            InvalidStatusTransitionException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateOpportunityException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateOpportunity(
            DuplicateOpportunityException ex,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request);
    }

}
