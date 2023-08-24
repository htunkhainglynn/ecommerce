package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts
            (@RequestParam(required = false) String keyword,
             @RequestParam(required = false) boolean isAvailable,
             @RequestParam Optional<Integer> page,
             @RequestParam Optional<Integer> size) {

        Page<ProductDto> result = productService.getAllProducts(keyword, isAvailable, page, size);
        return ResponseEntity.ok(result);
    }


    // Endpoint to get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> result = productService.getProductById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to create a new product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductDto product, HttpServletRequest request) throws Exception {
        // upload image & set url to product
        product.getProductVariants().forEach(
                productVariant -> {
                    try {
                        cloudinaryService.uploadAndSaveUrl(product.getSku(), productVariant, request);
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
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        Optional<ProductDto> productDto = productService.getProductById(id);
        if (productDto.isPresent()) {
            product.setId(id);
            ProductDto result = productService.saveProduct(product);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a product
    @DeleteMapping("/{id}")
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

