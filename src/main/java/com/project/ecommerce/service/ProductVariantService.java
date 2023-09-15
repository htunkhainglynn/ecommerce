package com.project.ecommerce.service;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.vo.ProductVariantVo;

import java.util.List;
import java.util.Optional;

public interface ProductVariantService {
    ProductVariantVo saveProductVariant(ProductVariantDto productVariantDto);

    List<ProductVariantVo> getProductVariantByProductId(Integer id);

    Optional<ProductVariantVo> getProductVariantById(Integer id);

    void deleteProductVariant(Integer id);

    Optional<String> getProductVariantImageUrl(Integer id);

    void updateExpenseHistory(Integer id, ExpenseDto expenseDto);

    Long getProductIdByPvId(Integer id);
}
