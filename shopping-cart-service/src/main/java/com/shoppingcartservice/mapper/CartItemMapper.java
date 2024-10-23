package com.shoppingcartservice.mapper;

import com.shoppingcartservice.dto.CartItemDto;
import com.shoppingcartservice.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDto toDto(CartItem cartItem);

}
