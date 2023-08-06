package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint to get all products
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) List<String> brands,
                            @RequestParam(required = false) String color,
                            @RequestParam(required = false) Integer minPrice,
                            @RequestParam(required = false) Integer maxPrice,
                            @RequestParam(required = false) List<String> productSizes,
                            @RequestParam(required = false) List<Double> ratings,
                            @RequestParam Optional<Integer> page,
                            @RequestParam Optional<Integer> size) {

        Page<ProductDto> result = productService.getAllProducts(
                name, category, brands, color, minPrice, maxPrice, productSizes, ratings, page, size);

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
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product) {
        log.info("productDto", product);
        ProductDto result = productService.saveProduct(product);
        return ResponseEntity.ok(result);
    }

    // Endpoint to update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        Optional<ProductDto> productDto = productService.getProductById(id);
        if(productDto.isPresent()) {
            product.setId(id);
            ProductDto result = productService.saveProduct(product);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<ProductDto> productDto = productService.getProductById(id);
        if(productDto.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

