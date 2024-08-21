package org.example.nazar.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(DuplicateProductFoundException.class)
    public ResponseEntity<String> handleDuplicateProductFoundException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: A product with the same details already exists.");
    }

    @ExceptionHandler(ProductTypeNotFoundException.class)
    public ResponseEntity<String> handleProductTypeNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Not Found: The specified product type does not exist.");
    }

    @ExceptionHandler(DuplicateTypeException.class)
    public ResponseEntity<String> handleDuplicateTypeException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: The input type already exists.");
    }

    @ExceptionHandler(DuplicateSiteException.class)
    public ResponseEntity<String> handleDuplicateSiteException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: The input site already exists.");
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: The username is already taken.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Not Found: " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        // Provide detailed validation failure information
        StringBuilder message = new StringBuilder("Validation failed: ");
        ex.getConstraintViolations().forEach(violation -> {
            message.append(violation.getPropertyPath())
                    .append(" - ")
                    .append(violation.getMessage())
                    .append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Display detailed validation errors
        StringBuilder errors = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherException(Exception e, WebRequest req) {
        // Provide a general error message with details
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + e.getMessage() + ". Request details: " + req.getDescription(false));
    }
}
