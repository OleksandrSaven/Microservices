package com.orderservice.repository;

import com.orderservice.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

}
