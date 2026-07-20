package com.taskspace.userservice.exception;

import com.taskspace.userservice.exception.user.EmailNotUniqueException;
import com.taskspace.userservice.exception.user.UserBadCredentialsException;
import com.taskspace.userservice.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCommonResponse> baseExceptionHandler(Throwable e) {
        log.error("Unexpected error", e);

        return ResponseEntity.status(500).body(ErrorCommonResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorCommonResponse> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        log.warn("Validation failed: {}", errorMessage);

        return ResponseEntity.badRequest().body(ErrorCommonResponse.builder()
                .errorMessage(errorMessage)
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorCommonResponse> userNotFoundHandler(UserNotFoundException e) {
        log.warn("User not found: {}", e.getMessage());

        return ResponseEntity.status(404).body(ErrorCommonResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    public ResponseEntity<ErrorCommonResponse> handleEmailNotUnique(EmailNotUniqueException e) {
        log.warn("Email is not unique: {}", e.getMessage());

        return ResponseEntity.status(409).body(ErrorCommonResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(UserBadCredentialsException.class)
    public ResponseEntity<ErrorCommonResponse> handleBadCredentials(UserBadCredentialsException e) {
        log.warn("Bad credentials: {}", e.getMessage());

        return ResponseEntity.status(401).body(ErrorCommonResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorCommonResponse> handlePasswordIncorrect(PasswordIncorrectException e) {
        log.warn("Password is incorrect: {}", e.getMessage());

        return ResponseEntity.status(401).body(ErrorCommonResponse.builder()
                .errorMessage(e.getMessage())
                .build());
    }
}
