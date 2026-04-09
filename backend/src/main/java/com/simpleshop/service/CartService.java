package com.simpleshop.service;

import com.simpleshop.dto.CartItemRequest;
import com.simpleshop.entity.Cart;
import com.simpleshop.entity.CartItem;
import com.simpleshop.entity.Product;
import com.simpleshop.entity.User;
import com.simpleshop.repository.CartRepository;
import com.simpleshop.repository.ProductRepository;
import com.simpleshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItem(Long userId, CartItemRequest request) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItem(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    public Cart clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
