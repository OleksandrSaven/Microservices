package com.userservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(this:: getErrorMessage)
                .collect(Collectors.toList());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timeStamp", LocalDateTime.now());
        response.put("statusCode", HttpStatus.BAD_REQUEST);
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            String field = ((FieldError) objectError).getField();
            String error = objectError.getDefaultMessage();
            return field + " " + error;
        }
        return objectError.getDefaultMessage();
    }
}
