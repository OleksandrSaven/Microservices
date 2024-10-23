package com.shoppingcartservice.controller;

import com.shoppingcartservice.dto.CartItemAddRequestDto;
import com.shoppingcartservice.dto.CartItemDto;
import com.shoppingcartservice.dto.CartItemUpdateDto;
import com.shoppingcartservice.dto.ShoppingCartDto;
import com.shoppingcartservice.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart controller")
@SecurityRequirement(name = "Authorization")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Hidden
    @PostMapping("/create")
    public void createShoppingCart(@RequestParam Long userId) {
        shoppingCartService.create(userId);
    }

    @PostMapping
    public CartItemDto addItemToShoppingCart(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @RequestBody CartItemAddRequestDto requestDto) {
        return shoppingCartService.addItemToShoppingCart(requestDto, userEmail);
    }

    @GetMapping
    public ShoppingCartDto findShoppingCart(
            @RequestHeader(name = "X-User-Email",required = false) String userEmail) {
        return shoppingCartService.findShoppingCart(userEmail);
    }

    @PutMapping("/items/{cartItemId}")
    public CartItemDto update(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateDto cartItemUpdateDto) {
        return shoppingCartService.update(userEmail, cartItemId, cartItemUpdateDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void delete(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @PathVariable Long cartItemId) {
        shoppingCartService.delete(cartItemId, userEmail);
    }

    @DeleteMapping("/items/delete")
    public void deleteItems(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail) {
        shoppingCartService.deleteAll(userEmail);
    }
}
