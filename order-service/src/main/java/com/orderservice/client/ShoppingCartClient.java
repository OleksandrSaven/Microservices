package com.orderservice.client;

import com.orderservice.dto.ShoppingCartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("shopping-cart-app")
public interface ShoppingCartClient {

    @GetMapping("api/cart")
    ShoppingCartDto findShoppingCart(@RequestHeader("X-User-Email") String userEmail);

    @DeleteMapping("api/cart/items/delete")
    void deleteCartItems(@RequestHeader("X-User-Email") String userEmail);
}
