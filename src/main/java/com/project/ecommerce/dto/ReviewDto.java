package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String content;
    private int rating;
    private Long product_id;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.product_id = review.getProduct().getId();
    }
}
