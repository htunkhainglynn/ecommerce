package com.project.ecommerce.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String content;
    private int rating;
}
