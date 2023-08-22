package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.Review;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ReviewRepository;
import com.project.ecommerce.service.ReviewService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;

    private final ProductRepository productRepo;

    private final ModelMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo,
                             ProductRepository productRepo,
                             ModelMapper mapper) {
        this.reviewRepo = reviewRepo;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepo.findAll().stream()
                .map(ReviewDto::new)  // custom mapper
                .toList();
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long id) {
        Optional<Review> optionalReview = reviewRepo.findById(id);
        return optionalReview.map(ReviewDto::new);
    }

    @Transactional
    @Override
    public ReviewDto saveReview(ReviewDto review) {
        Optional<Product> product = productRepo.findById(review.getProductId());
        if(product.isPresent()) {
            Review reviewEntity = mapper.map(review, Review.class);
            reviewEntity.setProduct(product.get());
            reviewRepo.save(reviewEntity);
            return new ReviewDto(reviewEntity);
        }
        return new ReviewDto();
    }

    @Transactional
    @Override
    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }
}
