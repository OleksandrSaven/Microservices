package com.orderservice.dto;

import java.util.Set;

public record ShoppingCartDto(
        Long userId,
        Set<CartItemDto> cartItems
) {
}
