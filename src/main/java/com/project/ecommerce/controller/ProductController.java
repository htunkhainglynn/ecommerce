package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.utils.PagerResult;
import com.project.ecommerce.vo.ProductVariantVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@Api(value = "Product Management")
public class ProductController {

    private final ProductService productService;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Requires ADMIN authority")
    public ResponseEntity<PagerResult<ProductDto>> getAllProducts
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) Boolean isAvailable,
             @RequestParam Optional<Integer> page,
             @RequestParam Optional<Integer> size) {

        Page<ProductDto> result = productService.getAllProducts(keyword, isAvailable == null || isAvailable, page, size);
        PagerResult<ProductDto> pagerResult = PagerResult.of(result);
        return ResponseEntity.ok(pagerResult);
    }


    // Endpoint to get a product by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get product by id", description = "Requires ADMIN authority")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> result = productService.getProductById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get product variants by product ID
    @GetMapping("/{id}/product-variants")
    @Operation(summary = "Get product variants by product id", description = "Requires ADMIN authority")
    public ResponseEntity<List<ProductVariantVo>> getProductVariantById(@PathVariable Integer id) {
        Optional<List<ProductVariantVo>> result = productService.getProductVariantById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new product
    @PostMapping
    @Operation(summary = "Create product", description = "Requires ADMIN authority")
    public ResponseEntity<ProductDto> createProduct(ProductDto product,
                                                    HttpServletRequest request) {
        // upload image & set url to each product variant
        product.getProductVariants().forEach(
                productVariant -> {
                    try {
                        cloudinaryService.uploadAndSetUrl(product.getSku(), productVariant, request);
                    } catch (Exception e) {
                        throw new ProductException("Error uploading image");
                    }
                }
        );

        ProductDto result = productService.saveProduct(product);
        return ResponseEntity.ok(result);
    }

    // Endpoint to update an existing product
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Requires ADMIN authority")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        Optional<ProductDto> productDto = productService.getProductById(id);
        if (productDto.isPresent()) {
            product.setId(id);
            ProductDto result = productService.saveProduct(product);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to update product availability
    @PutMapping("/{id}/availability")
    @Operation(summary = "Update product availability", description = "Requires ADMIN authority")
    public ResponseEntity<Void> updateProductAvailability(@PathVariable Long id) {
        productService.updateProductAvailability(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint to delete a product
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Requires ADMIN authority")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws Exception {
        Optional<ProductDto> productDto = productService.getProductById(id);
        if (productDto.isPresent()) {
            cloudinaryService.deleteFolder("product_" + productDto.get().getSku());
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

