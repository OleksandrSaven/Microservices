package com.orderservice.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDto {
    private Long id;
    private BigDecimal price;
    private int quantity;
}
