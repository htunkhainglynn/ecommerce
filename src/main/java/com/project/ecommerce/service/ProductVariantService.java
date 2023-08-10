package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;

import java.util.List;
import java.util.Optional;

public interface ProductVariantService {
    ProductVariantDto saveProductVariant(ProductVariantDto productVariantDto);

    void cacheProductVariant(ProductVariantCache productVariantCache);

    List<ProductVariantDto> getAllProductVariants();

    List<ProductVariantCache> getAllProductVariantCache(Integer id);

    Optional<ProductVariantDto> getProductVariantById(Integer id);
}
