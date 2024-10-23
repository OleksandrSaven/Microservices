package com.shoppingcartservice.service;

import com.shoppingcartservice.dto.CartItemAddRequestDto;
import com.shoppingcartservice.dto.CartItemDto;
import com.shoppingcartservice.dto.CartItemUpdateDto;
import com.shoppingcartservice.dto.ShoppingCartDto;

public interface ShoppingCartService {

    void create(Long userId);

    ShoppingCartDto findShoppingCart(String userEmail);

    CartItemDto addItemToShoppingCart(CartItemAddRequestDto requestDto, String userName);

    CartItemDto update(String userEmail, Long cartItemId, CartItemUpdateDto requestDto);

    void delete(Long cartItemId,String userEmail);

    void deleteAll(String userEmail);
}
