package com.simpleshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleshop.config.SecurityConfig;
import com.simpleshop.controller.GlobalExceptionHandler;
import com.simpleshop.entity.Category;
import com.simpleshop.entity.Product;
import com.simpleshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// integration tests for product controller
// @WebMvcTest loads only the web layer, no real db needed
// MockMvc lets us simulate http requests without starting a server
@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // converts objects to json and back

    @MockBean
    private ProductService productService; // fake service

    private final Category category = new Category(1L, "Electronics");
    private final Product product = new Product(1L, "Laptop", "Nice laptop", 999.99, "http://img.jpg", category);

    @Test
    void getAll_returns200WithProducts() throws Exception {
        when(productService.getAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(999.99));
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        when(productService.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getById_notFound_returns500() throws Exception {
        when(productService.getById(99L)).thenThrow(new RuntimeException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Product not found with id: 99"));
    }

    @Test
    void create_validProduct_returns201() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void update_validProduct_returns200() throws Exception {
        when(productService.update(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
