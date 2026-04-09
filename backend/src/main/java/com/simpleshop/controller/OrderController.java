package com.simpleshop.controller;

import com.simpleshop.dto.OrderRequest;
import com.simpleshop.entity.Order;
import com.simpleshop.repository.OrderRepository;
import com.simpleshop.repository.UserRepository;
import com.simpleshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(OrderService service,
                           OrderRepository orderRepository,
                           UserRepository userRepository) {
        this.service = service;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderRequest request) {
        Order order = service.createOrder(request);
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        String username = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUsername();

        List<Order> orders = orderRepository.findByCustomerName(username);
        return ResponseEntity.ok(orders);
    }
}
