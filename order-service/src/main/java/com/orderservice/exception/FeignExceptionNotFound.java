package com.orderservice.exception;

public class FeignExceptionNotFound extends RuntimeException {
    public FeignExceptionNotFound(String message) {
        super(message);
    }
}
