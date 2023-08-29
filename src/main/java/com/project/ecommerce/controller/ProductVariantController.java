package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.vo.ProductVariantVo;
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

import static org.springframework.http.ResponseEntity.ok;


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
        return ok(productVariantService.getAllProductVariants());
    }


    @PostMapping
    public ResponseEntity<ProductVariantVo> createProductVariant(ProductVariantDto productVariantDto,
                                                 HttpServletRequest request){
        Optional<ProductDto> product = productService.getProductById(productVariantDto.getProduct_id());
        if (product.isEmpty()) {
            throw new ProductException("Product not found");
        }

        try {
            cloudinaryService.uploadAndSetUrl(product.get().getSku(), productVariantDto, request);
        } catch (Exception e) {
            throw new ProductException("Error uploading image");
        }

        return ok(productVariantService.saveProductVariant(productVariantDto));
    }

    @PutMapping("/{id}")
    public ProductVariantVo updateProductVariant(@PathVariable Integer id, ProductVariantDto productVariantDto, HttpServletRequest request){
        productVariantDto.setId(id);
        Optional<ProductDto> product = productService.getProductById(productVariantDto.getProduct_id());
        if (product.isEmpty()) {
            throw new ProductException("Product not found");
        }

        // if image is updated, delete old image
        if (productVariantDto.getImageFile() != null) {
            productVariantService.getProductVariantImageUrl(id)
                    .ifPresent(cloudinaryService::deleteImage);
            try {
                cloudinaryService.uploadAndSetUrl(product.get().getSku(), productVariantDto, request);
            } catch (Exception e) {
                throw new ProductException("Error uploading image");
            }
        }

        return productVariantService.saveProductVariant(productVariantDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProductVariant(@PathVariable Integer id){
        productVariantService.getProductVariantImageUrl(id)
                .ifPresent(cloudinaryService::deleteImage);
        productVariantService.deleteProductVariant(id);
    }
}
