package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<ProductDto> getAllProducts();
    public Optional<ProductDto> getProduct(Long id);
    public ProductDto addProduct(ProductDto product);
    public ProductDto updateProduct(Long id);
    public void deleteProduct(Long id);

}
