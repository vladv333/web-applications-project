package com.simpleshop.service;

import com.simpleshop.entity.Category;
import com.simpleshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Category create(Category category) {
        return repository.save(category);
    }
}
