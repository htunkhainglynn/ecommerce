package com.project.ecommerce.service;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.entitiy.Review;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    ReviewDto getReviewById(Long id);
    List<ReviewDto> getReviewsByProductId(Long productId);
    ReviewDto createReview(ReviewDto review);
    ReviewDto updateReview(Long id, ReviewDto review);
    void deleteReview(Long id);
}
