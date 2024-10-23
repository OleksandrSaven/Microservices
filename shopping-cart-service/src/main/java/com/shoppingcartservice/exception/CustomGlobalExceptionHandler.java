package com.shoppingcartservice.exception;

import feign.FeignException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignExceptionNotFound(FeignException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handlePermissionException(PermissionException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
