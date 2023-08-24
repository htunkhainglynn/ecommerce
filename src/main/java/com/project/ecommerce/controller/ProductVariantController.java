package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.dto.ProductVariantVo;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ProductVariantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {
    private final ProductVariantService  productVariantService;

    private final CloudinaryService cloudinaryService;

    private final ProductService productService;

    @Autowired
    public ProductVariantController(ProductVariantService productVariantService,
                                    ProductService productService,
                                    CloudinaryService cloudinaryService) {
        this.productVariantService = productVariantService;
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantVo>> getAllProductVariants() {
        return ResponseEntity.ok(productVariantService.getAllProductVariants());
    }


    @PostMapping
    public ProductVariantVo createProductVariant(@ModelAttribute ProductVariantDto productVariantDto, HttpServletRequest request){
        Optional<ProductDto> product = productService.getProductById(productVariantDto.getProduct_id());
        if (product.isEmpty()) {
            throw new ProductException("Product not found");
        }

        try {
            cloudinaryService.uploadAndSaveUrl(product.get().getSku(), productVariantDto, request);
        } catch (Exception e) {
            throw new ProductException("Error uploading image");
        }

        return productVariantService.saveProductVariant(productVariantDto);
    }

    @PutMapping
    public ProductVariantVo updateProductVariant(@RequestBody ProductVariantDto productVariantDto){
        return productVariantService.saveProductVariant(productVariantDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProductVariant(@PathVariable Integer id){
        productVariantService.getProductVariantImageUrl(id)
                .ifPresent(cloudinaryService::deleteImage);
        productVariantService.deleteProductVariant(id);
    }
}
