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

    private String imageUrl;

    private boolean inStock;

    private boolean available;

    private List<ProductVariant> productVariants;

    private Organization organization;

    private List<Review> reviews;

    private double averageRating;

}
