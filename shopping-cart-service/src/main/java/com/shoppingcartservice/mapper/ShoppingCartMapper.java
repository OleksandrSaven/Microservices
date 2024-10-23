package com.shoppingcartservice.mapper;

import com.shoppingcartservice.dto.ShoppingCartDto;
import com.shoppingcartservice.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
