package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.CategoryDto;
import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.repo.CategoryRepository;
import com.project.ecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository repo;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return repo.findAll().stream()
                .map(category -> mapper.map(category, CategoryDto.class))
                .toList();
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long id) {
        Optional<Category> optionalCategory = repo.findById(id);
        Optional<CategoryDto> categoryDto = optionalCategory.map(category -> mapper.map(category, CategoryDto.class));
        return categoryDto;
    }

    @Override
    public CategoryDto saveCategory(CategoryDto category) {
        Category categoryEntity = mapper.map(category, Category.class);
        repo.save(categoryEntity);
        return mapper.map(categoryEntity, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        repo.deleteById(id);
    }
}
