package com.simpleshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleshop.config.SecurityConfig;
import com.simpleshop.dto.CartItemRequest;
import com.simpleshop.entity.*;
import com.simpleshop.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private final User user = new User(1L, "vlad", "vlad@gmail.com", "hashed");
    private final Cart cart = new Cart(1L, user, new ArrayList<>());

    @Test
    void getCart_returns200() throws Exception {
        when(cartService.getCartByUserId(1L)).thenReturn(cart);

        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addItem_validRequest_returns200() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(cartService.addItem(eq(1L), any(CartItemRequest.class))).thenReturn(cart);

        mockMvc.perform(post("/api/cart/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void removeItem_returns200() throws Exception {
        when(cartService.removeItem(1L, 1L)).thenReturn(cart);

        mockMvc.perform(delete("/api/cart/1/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    void clearCart_returns200() throws Exception {
        when(cartService.clearCart(1L)).thenReturn(cart);

        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk());
    }
}