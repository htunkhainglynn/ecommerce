package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.vo.ProductVariantVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    Page<ProductDto> getAllProducts(String keyword, Boolean isAvailable, Optional<Integer> page,
                                           Optional<Integer> size);
    Optional<ProductDto> getProductById(Long id);
    ProductDto saveProduct(ProductDto product);
    void deleteProduct(Long id);

    void updateProductVariantQuantity(Map<Integer, Integer> productQuantityMap);

    void updateProductAvailability(Long id);

    Optional<List<ProductVariantVo>> getProductVariantByProductId(Integer id);
}
