package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ReviewDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.Review;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ReviewRepository;
import com.project.ecommerce.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepo.findAll().stream()
                .map(ReviewDto::new)  // custom mapper
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ReviewDto> getReviewById(Long id) {
        Optional<Review> optionalReview = reviewRepo.findById(id);
        return optionalReview.map(ReviewDto::new);
    }

    @Override
    public ReviewDto saveReview(ReviewDto review) {
        Product product = productRepo.getReferenceById(review.getProductId());

        Review reviewEntity = mapper.map(review, Review.class);
        reviewEntity.setProduct(product);
        reviewRepo.save(reviewEntity);
        return new ReviewDto(reviewEntity);
    }

    @Transactional
    @Override
    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        return reviewRepo.findByProductId(productId)
                .stream()
                .map(ReviewDto::new)
                .toList();
    }
}
