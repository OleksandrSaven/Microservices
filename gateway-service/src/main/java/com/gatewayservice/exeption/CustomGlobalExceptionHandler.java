package com.gatewayservice.exeption;

import feign.FeignException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<Object> jwtExceptionHandler(CustomJwtException exception) {
        var response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
}
