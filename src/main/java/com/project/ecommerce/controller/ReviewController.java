package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        Optional<ReviewDto> result = reviewService.getReviewById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        Optional<ProductDto> product = productService.getProductById(productId);
        if (product.isPresent()) {
            return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewDto> createReviewByProductId(@PathVariable Long productId, @RequestBody ReviewDto review) {
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
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto review) {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if(reviewDto.isPresent()) {
            review.setId(id);
            ReviewDto result = reviewService.saveReview(review);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
