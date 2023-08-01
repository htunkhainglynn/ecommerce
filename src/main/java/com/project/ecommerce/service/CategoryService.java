package com.project.ecommerce.service;

import com.project.ecommerce.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(CategoryDto category);
    CategoryDto updateCategory(Long id, CategoryDto category);
    void deleteCategory(Long id);
}
