package com.shoppingcartservice.repository;

import com.shoppingcartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
