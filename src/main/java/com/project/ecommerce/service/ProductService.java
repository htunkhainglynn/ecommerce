package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<ProductDto> getAllProducts();
    public Optional<ProductDto> getProductById(Long id);
    public ProductDto saveProduct(ProductDto product);
    public void deleteProduct(Long id);

}
