package com.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity,
        BigDecimal price
) {
}
