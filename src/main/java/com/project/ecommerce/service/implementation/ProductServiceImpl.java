package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repo;

    @Override
    public List<ProductDto> getAllProducts() {
        return null;
    }

    @Override
    public Optional<ProductDto> getProduct(Long id) {
        return Optional.empty();
    }

    @Override
    public ProductDto addProduct(ProductDto product) {
        return null;
    }

    @Override
    public ProductDto updateProduct(Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
