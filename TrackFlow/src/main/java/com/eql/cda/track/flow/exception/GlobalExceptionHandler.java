package com.eql.cda.track.flow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Provides centralized exception handling across all @RequestMapping methods.
 * This class captures specific exceptions and transforms them into structured,
 * client-friendly HTTP responses. The order of the handlers matters: most
 * specific exceptions should be handled before more generic ones.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    /**
     * Handles validation errors triggered by @Valid on request bodies.
     * This is a specific handler for validation failures.
     *
     * @param ex The exception thrown when validation fails.
     * @return A ResponseEntity with a 400 Bad Request status and a structured error body.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles business logic exceptions for when a user already exists.
     * It uses the MessageSource to create a localized error message.
     *
     * @param ex The custom exception thrown when a registration conflict is detected.
     * @return A ResponseEntity with a 409 Conflict status and a clear, localized error message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        String message = messageSource.getMessage(
                "error.user.exists",
                new Object[]{ex.getLogin()},
                Locale.FRENCH
        );

        logger.warn("Registration conflict: {}", message);
        Map<String, String> response = Collections.singletonMap("error", message);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles unexpected, general exceptions as a last resort.
     * This catch-all handler must be placed after all specific handlers.
     *
     * @param ex The unhandled exception.
     * @return A ResponseEntity with a 500 Internal Server Error status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        Map<String, String> response = Collections.singletonMap("error", "An internal server error occurred. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}