package com.orderservice.dto;

import com.orderservice.model.Status;

public record ChangeStatusRequestDto(
        Status status
) {
}
