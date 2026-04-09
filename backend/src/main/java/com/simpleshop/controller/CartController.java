package com.simpleshop.controller;

import com.simpleshop.dto.CartItemRequest;
import com.simpleshop.entity.Cart;
import com.simpleshop.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/items")
    public Cart addItem(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        return cartService.addItem(userId, request);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public Cart removeItem(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/{userId}")
    public Cart clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}
