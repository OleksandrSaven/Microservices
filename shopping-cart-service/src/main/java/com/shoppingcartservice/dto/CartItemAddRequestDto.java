package com.shoppingcartservice.dto;

public record CartItemAddRequestDto(
        Long bookId,
        Integer quantity
) {
}
