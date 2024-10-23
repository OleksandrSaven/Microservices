package com.shoppingcartservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
}
