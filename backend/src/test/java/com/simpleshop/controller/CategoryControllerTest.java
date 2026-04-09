package com.simpleshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleshop.config.SecurityConfig;
import com.simpleshop.entity.Category;
import com.simpleshop.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAll_returns200WithCategories() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(
                new Category(1L, "Electronics"),
                new Category(2L, "Clothing")
        ));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Clothing"));
    }

    @Test
    void create_validCategory_returns200() throws Exception {
        Category cat = new Category(null, "Books");
        Category saved = new Category(1L, "Books");
        when(categoryService.create(any(Category.class))).thenReturn(saved);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Books"));
    }
}
