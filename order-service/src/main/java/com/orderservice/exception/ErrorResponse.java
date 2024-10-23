package com.orderservice.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        LocalDateTime date,
        HttpStatus status,
        List<String> errors
) {
}
