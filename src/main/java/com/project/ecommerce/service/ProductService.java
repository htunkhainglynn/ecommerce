package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.vo.ProductVariantVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    public Page<ProductDto> getAllProducts(String keyword, Boolean isAvailable, Optional<Integer> page,
                                           Optional<Integer> size);
    public Optional<ProductDto> getProductById(Long id);
    public ProductDto saveProduct(ProductDto product);
    public void deleteProduct(Long id);

    void updateProductQuantity(Map<Integer, Integer> productQuantityMap);

    void updateProductAvailability(Long id);

    Optional<List<ProductVariantVo>> getProductVariantById(Integer id);
}
