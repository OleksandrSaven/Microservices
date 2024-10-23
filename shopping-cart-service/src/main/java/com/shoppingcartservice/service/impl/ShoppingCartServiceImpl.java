package com.shoppingcartservice.service.impl;

import com.shoppingcartservice.client.BookClient;
import com.shoppingcartservice.client.UserClient;
import com.shoppingcartservice.dto.BookDto;
import com.shoppingcartservice.dto.CartItemAddRequestDto;
import com.shoppingcartservice.dto.CartItemDto;
import com.shoppingcartservice.dto.CartItemUpdateDto;
import com.shoppingcartservice.dto.ShoppingCartDto;
import com.shoppingcartservice.exception.EntityNotFoundException;
import com.shoppingcartservice.exception.PermissionException;
import com.shoppingcartservice.mapper.CartItemMapper;
import com.shoppingcartservice.mapper.ShoppingCartMapper;
import com.shoppingcartservice.model.CartItem;
import com.shoppingcartservice.model.ShoppingCart;
import com.shoppingcartservice.repository.CartItemRepository;
import com.shoppingcartservice.repository.ShoppingCartRepository;
import com.shoppingcartservice.service.ShoppingCartService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserClient userClient;
    private final BookClient bookClient;

    @Override
    public void create(Long userId) {
        var shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto findShoppingCart(String userEmail) {
        // I have a userEmail, but I need the user id
        // make a request to get a user id
        Long userId = userClient.getUserId(userEmail);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shoppingCart which belong user with id: " + userId));
        // need to find info about book title
        // I need make a list of books_id and after make a request and get book title for all items
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        List<Long> booksId = cartItems.stream()
                .map(CartItem::getBookId)
                .toList();
        List<BookDto> books = bookClient.findByCriteria(booksId);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.cartItems()
                .forEach(i -> i.setBookTitle(
                        books.stream()
                                .filter(b -> b.id().equals(i.getBookId()))
                                .findFirst().orElseThrow(
                                        () -> new EntityNotFoundException(
                                                "Can't find title for book by id: "
                                                        + i.getBookId()))
                                .title()
                ));
        return shoppingCartDto;
    }

    @Override
    @Transactional
    public CartItemDto addItemToShoppingCart(CartItemAddRequestDto requestDto, String userEmail) {
        // find shoppingCart which belong to current user
        // I have a user email, but I need userId -> make a request and get userId
        Long userId = userClient.getUserId(userEmail);
        
        // find shoppingCart which belong to user with id := userId
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shoppingCart which belong user with id: " + userId));
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        //make a request to the book-service and return book title
        cartItem.setBookId(requestDto.bookId());
        cartItem.setQuantity(requestDto.quantity());
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        if (shoppingCart.getCartItems().isEmpty()) {
            shoppingCart.setCartItems(cartItems);
        } else {
            shoppingCart.getCartItems().add(cartItem);
        }
        BookDto bookDto = bookClient.findById(requestDto.bookId());
        CartItemDto cartItemDto = cartItemMapper.toDto(cartItemRepository.save(cartItem));
        cartItemDto.setBookTitle(bookDto.title());
        return cartItemDto;
    }

    @Override
    @Transactional
    public CartItemDto update(String userEmail, Long cartItemId, CartItemUpdateDto requestDto) {
        // check if item belong to current user
        Long userId = userClient.getUserId(userEmail);
        // get all items belong to user
        // find shoppingCart and get cartItems belong authenticated user
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shoppingCart by id: " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by id: " + cartItemId));
        if (!cartItems.contains(cartItem)) {
            throw new PermissionException("You don't have permission to change this cartItem");
        }
        CartItem requiredItem = cartItems.stream()
                .filter(i -> i.getId().equals(cartItemId)).findFirst().get();
        requiredItem.setQuantity(requestDto.quantity());

        BookDto bookDto = bookClient.findById(requiredItem.getBookId());

        CartItemDto cartItemDto = cartItemMapper.toDto(cartItemRepository.save(requiredItem));
        cartItemDto.setBookTitle(bookDto.title());
        return cartItemDto;
    }

    @Override
    public void delete(Long cartItemId, String userEmail) {
        Long userId = userClient.getUserId(userEmail);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shoppingCart which belong user with id: " + userId)
        );
        // next step - check belong cartItem current user
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Long id = cartItems.stream()
                .map(CartItem::getId)
                .filter(e -> e.equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id: " + cartItemId));

        shoppingCart.getCartItems().remove(shoppingCart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found")));
        cartItemRepository.deleteById(id);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteAll(String userEmail) {
        Long userId = userClient.getUserId(userEmail);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shoppingCart which belong user with id: " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        shoppingCart.getCartItems().clear();
        cartItems.forEach(cartItemRepository::delete);
        shoppingCartRepository.save(shoppingCart);
    }
}
