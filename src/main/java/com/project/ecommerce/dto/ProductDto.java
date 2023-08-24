package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private boolean inStock;

    private boolean available;

    private List<ProductVariantDto> productVariants = new ArrayList<>();

    private Organization organization;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Review> reviews;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double averageRating;

    public ProductDto(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.inStock = entity.isInStock();
        this.available = entity.isAvailable();
        this.organization = entity.getOrganization();
        this.reviews = entity.getReviews();
        this.averageRating = entity.getAverageRating();
        entity.getProductVariants().forEach(productVariant -> {
            this.productVariants.add(new ProductVariantDto(productVariant));
        });
    }

}
