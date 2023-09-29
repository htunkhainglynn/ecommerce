package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
@Api(value = "Review Management")
public class ReviewController {

    private final ReviewService reviewService;

    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Requires ADMIN authority")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable int id) {
        Optional<ReviewDto> result = reviewService.getReviewById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get reviews by product id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        Optional<ProductDto> product = productService.getProductById(productId);
        if (product.isPresent()) {
            return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/product/{productId}")
    @Operation(summary = "Create review by product id", description = "Requires USER authority")
    public ResponseEntity<ReviewDto> createReviewByProductId(@PathVariable Long productId, @RequestBody @Validated ReviewDto review) {
        Optional<ProductDto> product = productService.getProductById(productId);
        if (product.isPresent()) {
            review.setProductId(productId);
            ReviewDto result = reviewService.saveReview(review);
            return ResponseEntity
                    .created(URI.create("/api/v1/reviews/%d".formatted(result.getId())))
                    .body(result);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update review by id", description = "Requires USER authority")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable int id, @RequestBody ReviewDto review) {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if(reviewDto.isPresent()) {
            review.setId(id);
            ReviewDto result = reviewService.saveReview(review);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review by id", description = "Requires USER authority")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
