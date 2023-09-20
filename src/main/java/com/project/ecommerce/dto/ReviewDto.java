package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDto {
    private int id;
    private String content;
    private int rating;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // use only for post
    private Long productId;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
    }
}
