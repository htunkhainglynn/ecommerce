package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String imageUrl;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long category_id;

    private Category category;
    private String brand;

    private String size;

    private String color;

    private int variants;
    private List<Review> reviews;
    private double averageRating;

}
