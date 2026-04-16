package com.simpleshop.service;

import com.simpleshop.dto.CartItemRequest;
import com.simpleshop.entity.*;
import com.simpleshop.repository.CartRepository;
import com.simpleshop.repository.ProductRepository;
import com.simpleshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService service;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User(1L, "vlad", "vlad@gmail.com", "hashed");
        Category cat = new Category(1L, "Electronics");
        product = new Product(1L, "Laptop", "Nice", 999.99, "http://img.jpg", cat);
        cart = new Cart(1L, user, new ArrayList<>());
    }

    @Test
    void getCartByUserId_existingCart_returnsCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Cart result = service.getCartByUserId(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getCartByUserId_noCart_createsNewCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = service.getCartByUserId(1L);

        // should create and save a new cart
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItem_newProduct_addsToCart() {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = service.addItem(1L, request);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProduct().getName()).isEqualTo("Laptop");
        verify(cartRepository).save(cart);
    }

    @Test
    void addItem_existingProduct_increasesQuantity() {
        // add item to cart first
        CartItem existingItem = new CartItem(1L, cart, product, 1);
        cart.getItems().add(existingItem);

        CartItemRequest request = new CartItemRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        service.addItem(1L, request);

        // quantity should be 2 now, not a new item
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void removeItem_removesCorrectProduct() {
        CartItem item = new CartItem(1L, cart, product, 2);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        service.removeItem(1L, 1L);

        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void clearCart_removesAllItems() {
        cart.getItems().add(new CartItem(1L, cart, product, 1));
        cart.getItems().add(new CartItem(2L, cart, product, 3));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        service.clearCart(1L);

        assertThat(cart.getItems()).isEmpty();
    }
}