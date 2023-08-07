package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.service.ProductVariantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {


    private final ProductVariantService  productVariantService;

    @Autowired
    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @PostMapping
    public ProductVariantDto createProductVariant(@RequestBody ProductVariantDto productVariantDto){
        return productVariantService.createProductVariant(productVariantDto);
    }

}
