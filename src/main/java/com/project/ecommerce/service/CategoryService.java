package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    Optional<CategoryDto> getCategoryById(Long id);
    CategoryDto saveCategory(CategoryDto category);
    void deleteCategory(Long id);
}
