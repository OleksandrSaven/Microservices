package com.orderservice.dto;

public record CartItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
