package com.bookservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDto(
        @NotBlank
        String name,
        String description
) {
}
