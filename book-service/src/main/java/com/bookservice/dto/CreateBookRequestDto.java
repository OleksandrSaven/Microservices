package com.bookservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import org.hibernate.validator.constraints.ISBN;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @ISBN
        String isbn,
        @NotNull
        @Min(0)
        BigDecimal price,
        String coverImage,
        String description,
        Set<Long> categoryIds
) {
}
