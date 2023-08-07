package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;

import java.util.List;

public interface ProductVariantService {
    ProductVariantDto createProductVariant(ProductVariantDto productVariantDto);

    void cacheProductVariant(ProductVariantCache productVariantCache);

    List<ProductVariantDto> getAllProductVariants();

    List<ProductVariantCache> getAllProductVariantCache();

}
