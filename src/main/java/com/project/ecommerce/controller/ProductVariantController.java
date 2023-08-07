package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.service.ProductVariantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {

    private final ProductVariantService  productVariantService;

    @Autowired
    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantDto>> getAllProductVariants() {
        return ResponseEntity.ok(productVariantService.getAllProductVariants());
    }

    @PostMapping
    public ProductVariantDto createProductVariant(@RequestBody ProductVariantDto productVariantDto){
        return productVariantService.createProductVariant(productVariantDto);
    }

    @PostMapping("/cache")
    public void addProductVariantToCache(@RequestBody ProductVariantCache productVariantCache) {
        productVariantService.cacheProductVariant(productVariantCache);
    }

    @GetMapping("/cache")
    public ResponseEntity<List<ProductVariantCache>> getAllProductVariantCache() {
        return ResponseEntity.ok(productVariantService.getAllProductVariantCache());
    }

}
