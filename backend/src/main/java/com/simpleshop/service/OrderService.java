package com.simpleshop.service;

import com.simpleshop.dto.OrderRequest;
import com.simpleshop.entity.Order;
import com.simpleshop.entity.OrderItem;
import com.simpleshop.entity.Product;
import com.simpleshop.repository.OrderRepository;
import com.simpleshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (OrderRequest.ItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemReq.getProductId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice() * itemReq.getQuantity()); // calc line total

            items.add(item);
            total += item.getPrice();
        }

        order.setItems(items);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }
}
