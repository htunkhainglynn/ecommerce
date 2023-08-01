package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.repo.ReviewRepository;
import com.project.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repo;

    @Override
    public List<ReviewDto> getAllReviews() {
        return null;
    }

    @Override
    public ReviewDto getReviewById(Long id) {
        return null;
    }

    @Override
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        return null;
    }

    @Override
    public ReviewDto createReview(ReviewDto review) {
        return null;
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto review) {
        return null;
    }

    @Override
    public void deleteReview(Long id) {

    }
}
