package com.gatewayservice.exeption;

public class FeignExceptionNotFound extends RuntimeException {
    public FeignExceptionNotFound(String message) {
        super(message);
    }
}
