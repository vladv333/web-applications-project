package com.simpleshop.service;

import com.simpleshop.dto.OrderRequest;
import com.simpleshop.entity.Category;
import com.simpleshop.entity.Order;
import com.simpleshop.entity.Product;
import com.simpleshop.repository.OrderRepository;
import com.simpleshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService service;

    private Product product;

    @BeforeEach
    void setUp() {
        Category cat = new Category(1L, "Electronics");
        product = new Product(1L, "Laptop", "Nice laptop", 999.99, "http://img.jpg", cat);
    }

    @Test
    void createOrder_validRequest_createsOrder() {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("John Doe");

        OrderRequest.ItemRequest item = new OrderRequest.ItemRequest();
        item.setProductId(1L);
        item.setQuantity(2);
        request.setItems(List.of(item));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // return whatever order was passed to save
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = service.createOrder(request);

        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        assertThat(result.getTotalPrice()).isEqualTo(1999.98); // 999.99 * 2
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void createOrder_productNotFound_throwsException() {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Jane");

        OrderRequest.ItemRequest item = new OrderRequest.ItemRequest();
        item.setProductId(99L);
        item.setQuantity(1);
        request.setItems(List.of(item));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 99");
    }
}
