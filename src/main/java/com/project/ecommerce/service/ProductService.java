package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public Page<ProductDto> getAllProducts(String keyword, boolean isAvailable, Optional<Integer> page,
                                           Optional<Integer> size);
    public Optional<ProductDto> getProductById(Long id);
    public ProductDto saveProduct(ProductDto product);
    public void deleteProduct(Long id);
}
