package com.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("shopping-cart-app")
public interface ShoppingCartClient {

    @PostMapping("/api/cart/create")
    void createShoppingCart(@RequestParam Long userId);
}
