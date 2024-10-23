package com.orderservice.service;

import com.orderservice.dto.ChangeStatusRequestDto;
import com.orderservice.dto.CreateOrderRequestDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.OrderItemDto;
import java.util.List;

public interface OrderService {

    OrderDto create(String userEmail, CreateOrderRequestDto requestDto);

    List<OrderDto> findAll(String userEmail);

    OrderDto changeStatus(Long orderId, ChangeStatusRequestDto requestDto);

    List<OrderItemDto> findItemSpecificOrder(String userEmail, Long orderId);

    OrderItemDto findSpecificItemSpecificOrder(String userEmail, Long orderId, Long itemId);

}
