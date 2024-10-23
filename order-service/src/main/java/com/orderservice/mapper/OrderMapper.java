package com.orderservice.mapper;

import com.orderservice.dto.BookDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.OrderItemDto;
import com.orderservice.model.Order;
import com.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "bookId", source = "id")
    @Mapping(target = "id", ignore = true)
    OrderItem toModel(BookDto bookDto);

    OrderDto toDto(Order order);

    OrderItemDto toDto(OrderItem orderItem);

}
