package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ExpenseService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ProductVariantService;
import com.project.ecommerce.vo.ProductVariantVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/v1/product-variants")
@Api(value = "Product Variant Management")
@Slf4j
public class ProductVariantController {
    private final ProductVariantService  productVariantService;

    private final ProductService productService;

    private final ExpenseService expenseService;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductVariantController(ProductVariantService productVariantService,
                                    ProductService productService,
                                    ExpenseService expenseService,
                                    CloudinaryService cloudinaryService) {
        this.productVariantService = productVariantService;
        this.productService = productService;
        this.expenseService = expenseService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product variant by id", description = "Requires ADMIN authority")
    public ResponseEntity<ProductVariantVo> getProductVariantById(@PathVariable Integer id) {
        Optional<ProductVariantVo> result = productVariantService.getProductVariantById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create product variant", description = "Requires ADMIN authority")
    public ResponseEntity<ProductVariantVo> createProductVariant(ProductVariantDto productVariantDto,
                                                 HttpServletRequest request){
        Optional<ProductDto> product = productService.getProductById(productVariantDto.getProductId());
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
        Optional<ProductDto> product = productService.getProductById(productVariantDto.getProductId());
        if (product.isEmpty()) {
            throw new ProductException("Product not found");
        }

        // if image is updated, delete old image
        if (!productVariantDto.getImageFile().isEmpty()) {
            productVariantService.getProductVariantImageUrl(id)
                    .ifPresent(cloudinaryService::deleteImage);
            try {
                cloudinaryService.uploadAndSetUrl(product.get().getSku(), productVariantDto, request);
            } catch (Exception e) {
                throw new ProductException("Error uploading image");
            }
        }

        // set updated date
        productVariantDto.setUpdatedAt(LocalDate.now());

        return productVariantService.saveProductVariant(productVariantDto);
    }

    @GetMapping("{id}/expense-history")
    @Operation(summary = "Get expense history of a product variant", description = "Requires ADMIN authority")
    public ResponseEntity<List<ExpenseDto>> getExpenseHistory(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(expenseService.getExpenseByProductVariantId(id));
    }

    @PutMapping("/expense-history/{id}")
    @Operation(summary = "To update old expense history if there was a typo.", description = "Requires ADMIN authority")
    public void updateExpenseHistory(@PathVariable(value = "id") Integer id, @RequestBody ExpenseDto expenseDto){
        expenseService.updateExpenseHistory(id, expenseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product variant", description = "Requires ADMIN authority")
    public void deleteProductVariant(@PathVariable Integer id){
        productVariantService.getProductVariantImageUrl(id)
                .ifPresent(cloudinaryService::deleteImage);

        // if all product variants of a product are deleted, set product to unavailable
        Long productId = productVariantService.getProductIdByPvId(id);
        productService.getProductVariantById(productId.intValue()).ifPresent(productVariantVos -> {
            if (productVariantVos.size() == 1) {
                productService.updateProductAvailability(productId);
            }
        });

        productVariantService.deleteProductVariant(id);
    }
}
