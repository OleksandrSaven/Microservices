package com.orderservice.dto;

import com.orderservice.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status,
        Set<OrderItemDto> orderItems
) {
}
