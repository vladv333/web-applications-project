package com.simpleshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleshop.config.SecurityConfig;
import com.simpleshop.dto.OrderRequest;
import com.simpleshop.entity.Order;
import com.simpleshop.repository.OrderRepository;
import com.simpleshop.repository.UserRepository;
import com.simpleshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createOrder_validRequest_returns201() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("John Doe");

        OrderRequest.ItemRequest item = new OrderRequest.ItemRequest();
        item.setProductId(1L);
        item.setQuantity(2);
        request.setItems(List.of(item));

        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("John Doe");
        order.setTotalPrice(1999.98);
        order.setCreatedAt(LocalDateTime.now());

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalPrice").value(1999.98));
    }
}