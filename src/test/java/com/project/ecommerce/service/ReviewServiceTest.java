package com.project.ecommerce.service;

import com.project.ecommerce.dto.ReviewDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql", "/sql/product.sql", "/sql/review.sql"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Order(1)
    @Test
    void getReviewByProductId() {
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(1L);

        assertThat(reviews, hasSize(2));
        assertThat(reviews.get(0).getRating(), is(5));
        assertThat(reviews.get(1).getRating(), is(3));
        assertThat(reviews.get(0).getContent(), is("This product is excellent!"));
        assertThat(reviews.get(1).getContent(), is("Its okay, not great."));
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/review/create.txt")
    void testCreateReview(Integer rating, Integer productId, String content) {
        ReviewDto reviewDto = ReviewDto.builder()
                .rating(rating)
                .productId((long)productId)
                .content(content)
                .build();
        reviewService.saveReview(reviewDto);

        List<ReviewDto> reviews = reviewService.getReviewsByProductId(1L);
        assertThat(reviews, hasSize(3));
        assertThat(reviews.get(2).getRating(), is(rating));
        assertThat(reviews.get(2).getContent(), is(content));
    }

    @Order(3)
    @Test
    void testDeleteReviewById() {
        reviewService.deleteReview(1L);
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(1L);
        assertThat(reviews, hasSize(1));
    }
}
