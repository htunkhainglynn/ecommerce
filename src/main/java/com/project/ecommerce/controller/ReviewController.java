package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        Optional<ReviewDto> result = reviewService.getReviewById(id);
        if(result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(result.get());
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewDto> createReviewByProductId(@PathVariable Long productId, @RequestBody ReviewDto review) {
        ReviewDto result = reviewService.saveReview(review);
        return ResponseEntity
                .created(URI.create("/api/v1/reviews/%d".formatted(result.getId())))
                .body(result);
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
