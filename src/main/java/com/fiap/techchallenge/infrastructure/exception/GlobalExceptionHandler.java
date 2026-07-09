package com.fiap.techchallenge.infrastructure.exception;

import com.fiap.techchallenge.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problem.setTitle("Resource not found");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/not-found")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(RestaurantOwnerException.class)
    public ProblemDetail handleRestaurantOwner(
            RestaurantOwnerException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.PRECONDITION_FAILED);

        problem.setTitle("Owner user type wrong");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/restaurant-owner")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail handleDuplicateEmail(
            DuplicateEmailException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate email");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/duplicate-email")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public ProblemDetail handleDuplicateLogin(
            DuplicateLoginException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate login");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/duplicate-login")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setTitle("Invalid credentials");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/unauthorized")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Business rule violation");
        problem.setDetail(ex.getMessage());
        problem.setType(
                URI.create("https://api.techchallenge/errors/business-rule")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation error");
        problem.setDetail("One or more fields are invalid");
        problem.setType(
                URI.create("https://api.techchallenge/errors/validation")
        );

        Map<String, String> fields = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fields.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        addDefaultProperties(problem, request);
        problem.setProperty("fields", fields);

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("Internal server error");
        problem.setDetail("Unexpected error occurred");
        problem.setType(
                URI.create("https://api.techchallenge/errors/internal")
        );
        addDefaultProperties(problem, request);

        return problem;
    }

    private void addDefaultProperties(
            ProblemDetail problem,
            HttpServletRequest request
    ) {
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("path", request.getRequestURI());
    }
}