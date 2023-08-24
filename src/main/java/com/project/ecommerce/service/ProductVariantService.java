package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.dto.ProductVariantVo;

import java.util.List;
import java.util.Optional;

public interface ProductVariantService {
    ProductVariantVo saveProductVariant(ProductVariantDto productVariantDto);

    void cacheProductVariant(ProductVariantCache productVariantCache);

    List<ProductVariantVo> getAllProductVariants();

    List<ProductVariantCache> getAllProductVariantCache(Integer id);

    Optional<ProductVariantVo> getProductVariantById(Integer id);

    void deleteProductVariant(Integer id);

    Optional<String> getProductVariantImageUrl(Integer id);
}
