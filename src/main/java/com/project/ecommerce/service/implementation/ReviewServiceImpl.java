package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.entitiy.Review;
import com.project.ecommerce.repo.ReviewRepository;
import com.project.ecommerce.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repo;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<ReviewDto> getAllReviews() {
        return repo.findAll().stream()
                .map(review -> mapper.map(review, ReviewDto.class))
                .toList();
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long id) {
        Optional<Review> optionalReview = repo.findById(id);
        return optionalReview.map(review -> mapper.map(review, ReviewDto.class));
    }

    @Override
    public ReviewDto saveReview(ReviewDto review) {
        Review reviewEntity = mapper.map(review, Review.class);
        repo.save(reviewEntity);
        return mapper.map(reviewEntity, ReviewDto.class);
    }

    @Override
    public void deleteReview(Long id) {
        repo.deleteById(id);
    }
}
