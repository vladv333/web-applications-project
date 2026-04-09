package com.simpleshop.service;

import com.simpleshop.entity.Category;
import com.simpleshop.entity.Product;
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

// unit tests for product service
// using mockito to fake the repository so we dont need a real db
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository; // fake repo

    @InjectMocks
    private ProductService service; // real service with fake repo injected

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics");
        product = new Product(1L, "Laptop", "Nice laptop", 999.99, "http://img.jpg", category);
    }

    @Test
    void getAll_returnsListOfProducts() {
        when(repository.findAll()).thenReturn(List.of(product));

        List<Product> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop");
    }

    @Test
    void getById_existingId_returnsProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
    }

    @Test
    void getById_nonExistingId_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // make sure exception is thrown with correct message
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 99");
    }

    @Test
    void create_savesAndReturnsProduct() {
        when(repository.save(product)).thenReturn(product);

        Product result = service.create(product);

        assertThat(result.getName()).isEqualTo("Laptop");
        verify(repository, times(1)).save(product); // check save was called once
    }

    @Test
    void update_existingId_updatesProduct() {
        Product updated = new Product(null, "Laptop Pro", "Updated desc", 1199.99, "http://new.jpg", category);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        service.update(1L, updated);

        verify(repository).save(any(Product.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
