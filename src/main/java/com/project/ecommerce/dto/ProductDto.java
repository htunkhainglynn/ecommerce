package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.entitiy.Review;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private MultipartFile imageUrl;
    private Category category;
    private List<Review> reviews;
}
