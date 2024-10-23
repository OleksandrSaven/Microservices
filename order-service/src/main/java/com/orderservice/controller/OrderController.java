package com.orderservice.controller;

import com.orderservice.dto.ChangeStatusRequestDto;
import com.orderservice.dto.CreateOrderRequestDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.OrderItemDto;
import com.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Order controller")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto create(@RequestHeader(name = "X-User-Email",required = false) String userEmail,
                        @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.create(userEmail, requestDto);
    }

    @GetMapping
    public List<OrderDto> findAll(@RequestHeader(
            value = "X-User-Email", required = false) String userEmail) {
        return orderService.findAll(userEmail);
    }

    @PatchMapping("{id}")
    public OrderDto changeStatus(@PathVariable Long id,
                                 @RequestBody ChangeStatusRequestDto requestDto) {
        return orderService.changeStatus(id, requestDto);
    }

    @GetMapping("{orderId}/items")
    public List<OrderItemDto> findItemSpecificOrder(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @PathVariable Long orderId) {
        return orderService.findItemSpecificOrder(userEmail, orderId);
    }

    @GetMapping("{orderId}/items/{itemId}")
    public OrderItemDto findSpecificItemSpecificOrder(
            @RequestHeader(name = "X-User-Email", required = false) String userEmail,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.findSpecificItemSpecificOrder(userEmail, orderId, itemId);
    }
}
