package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.CategoryDto;
import com.project.ecommerce.repo.CategoryRepository;
import com.project.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository repo;

    @Override
    public List<CategoryDto> getAllCategories() {
        return null;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return null;
    }

    @Override
    public CategoryDto createCategory(CategoryDto category) {
        return null;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto category) {
        return null;
    }

    @Override
    public void deleteCategory(Long id) {

    }
}
