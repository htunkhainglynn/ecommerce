package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.*;
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
    private int stock;
    private MultipartFile imageUrl;
    private CategoryDto category;
    private Brand brand;

    private List<Size> sizes;

    private List<Color> colors;
    private List<Review> reviews;
    private double averageRating;
}
