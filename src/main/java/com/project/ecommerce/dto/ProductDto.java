package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.entitiy.Review;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ProductDto {
    private Long id;

    private String name;
    private String description;
    private double price;
    private MultipartFile imageUrl;
    private Category category;
    private Review review;
}
