package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.vo.ProductVariantVo;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ProductVariantService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/v1/product-variants")
@Api(value = "Product Variant Management")
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
    @Operation(summary = "Get all product variants", description = "Requires ADMIN authority")
    public ResponseEntity<List<ProductVariantVo>> getAllProductVariants() {
        return ok(productVariantService.getAllProductVariants());
    }


    @PostMapping
    @Operation(summary = "Create product variant", description = "Requires ADMIN authority")
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
    @Operation(summary = "Update product variant", description = "Requires ADMIN authority")
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
    @Operation(summary = "Delete product variant", description = "Requires ADMIN authority")
    public void deleteProductVariant(@PathVariable Integer id){
        productVariantService.getProductVariantImageUrl(id)
                .ifPresent(cloudinaryService::deleteImage);
        productVariantService.deleteProductVariant(id);
    }
}
