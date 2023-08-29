package com.project.ecommerce.service;

import com.project.ecommerce.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    Optional<ReviewDto> getReviewById(Long id);
    ReviewDto saveReview(ReviewDto review);
    void deleteReview(Long id);

    List<ReviewDto> getReviewsByProductId(Long productId);
}
