package com.gatewayservice.exeption;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        LocalDateTime dateTime,
        HttpStatus errorCode,
        List<String> errorMessage
) {
}
