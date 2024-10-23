package com.orderservice.service.impl;

import com.orderservice.client.BookClient;
import com.orderservice.client.ShoppingCartClient;
import com.orderservice.client.UserClient;
import com.orderservice.dto.BookDto;
import com.orderservice.dto.CartItemDto;
import com.orderservice.dto.ChangeStatusRequestDto;
import com.orderservice.dto.CreateOrderRequestDto;
import com.orderservice.dto.OrderDto;
import com.orderservice.dto.OrderItemDto;
import com.orderservice.dto.ShoppingCartDto;
import com.orderservice.exception.EntityNotFoundException;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.model.Order;
import com.orderservice.model.OrderItem;
import com.orderservice.model.Status;
import com.orderservice.repository.OrderItemRepository;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartClient shoppingCartClient;
    private final BookClient bookClient;
    private final UserClient userClient;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(String userEmail, CreateOrderRequestDto requestDto) {
        // make a request and find shoppingCart current user and get items
        ShoppingCartDto shoppingCart = shoppingCartClient.findShoppingCart(userEmail);
        if (shoppingCart.cartItems().isEmpty()) {
            throw new EntityNotFoundException("Can't find any items in the shoppingCart");
        }
        Order order = setupOrder(shoppingCart, requestDto);
        deleteShoppingCartItems(userEmail);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAll(String userEmail) {
        Long userId = userClient.getUserId(userEmail);
        List<Order> all = orderRepository.findByUserId(userId);
        return all.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto changeStatus(Long orderId, ChangeStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id:" + orderId));
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> findItemSpecificOrder(String userEmail, Long orderId) {
        Long userId = userClient.getUserId(userEmail);
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find order by id which belong current user " + orderId));
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findSpecificItemSpecificOrder(String userEmail, Long orderId, Long itemId) {
        Long userId = userClient.getUserId(userEmail);
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find order by id which belong current user" + orderId));
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst().orElseThrow(() -> new EntityNotFoundException(
                        "Can't find item by id " + itemId));
        return orderMapper.toDto(orderItem);
    }

    private void deleteShoppingCartItems(String userEmail) {
        shoppingCartClient.deleteCartItems(userEmail);
    }

    private Order setupOrder(ShoppingCartDto shoppingCart, CreateOrderRequestDto requestDto) {
        Set<BookDto> bookDto = getBookDto(shoppingCart);
        Order order = new Order();
        order.setUserId(shoppingCart.userId());
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        order.setStatus(Status.PENDING);
        order.setTotal(calculateTotalPrice(bookDto));
        orderRepository.save(order);
        return saveOrderItem(order, bookDto);
    }

    private Order saveOrderItem(Order order, Set<BookDto> bookDto) {
        Set<OrderItem> orderItems = bookDto.stream()
                .map(orderMapper::toModel)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        return order;
    }

    private BigDecimal calculateTotalPrice(Set<BookDto> bookDto) {
        return bookDto.stream()
                .map(this::itemPrice)
                .reduce(BigDecimal::add).get();
    }

    private Set<BookDto> getBookDto(ShoppingCartDto shoppingCartDto) {
        List<Long> bookIds = shoppingCartDto.cartItems()
                .stream()
                .map(CartItemDto::bookId)
                .toList();
        // make a request to get the price
        Set<BookDto> bookDto = bookClient.findByCriteria(bookIds);
        bookDto.forEach(book -> book.setQuantity(
                shoppingCartDto.cartItems().stream()
                        .filter(item -> item.bookId().equals(book.getId()))
                        .findFirst()
                        .map(CartItemDto::quantity)
                        .orElse(0)));
        return bookDto;
    }

    private BigDecimal itemPrice(BookDto bookDto) {
        return bookDto.getPrice()
                .multiply(BigDecimal.valueOf(bookDto.getQuantity()));
    }
}
