package com.gatewayservice.exeption;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String message) {
        super(message);
    }
}
