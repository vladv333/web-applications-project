package com.simpleshop.service;

import com.simpleshop.entity.Category;
import com.simpleshop.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @Test
    void getAll_returnsAllCategories() {
        Category cat1 = new Category(1L, "Electronics");
        Category cat2 = new Category(2L, "Clothing");
        when(repository.findAll()).thenReturn(List.of(cat1, cat2));

        List<Category> result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Electronics");
    }

    @Test
    void create_savesCategory() {
        Category cat = new Category(null, "Books");
        Category saved = new Category(1L, "Books");
        when(repository.save(cat)).thenReturn(saved);

        Category result = service.create(cat);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Books");
        verify(repository, times(1)).save(cat);
    }
}
