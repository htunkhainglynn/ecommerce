package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ReviewDto> getReviewById(Long id) {
        Optional<ReviewDto> result = reviewService.getReviewById(id);
        if(result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(ReviewDto review) {
        ReviewDto result = reviewService.saveReview(review);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(Long id, ReviewDto review) {
        Optional<ReviewDto> reviewDto = reviewService.getReviewById(id);
        if(reviewDto.isPresent()) {
            review.setId(id);
            ReviewDto result = reviewService.saveReview(review);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
