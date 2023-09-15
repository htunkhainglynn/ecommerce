package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.*;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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

    private Integer sku;

    private Double weight;

    private String name;

    private String description;

    private boolean available;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ProductVariantDto> productVariants = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Organization organization;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double averageRating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double price;

    public ProductDto(Product entity) {
        this.id = entity.getId();
        this.sku = entity.getSku();
        this.weight = entity.getWeight();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.available = entity.isAvailable();
        this.organization = entity.getOrganization();
        this.averageRating = entity.getAverageRating();
        if (entity.getProductVariants() != null) {
            for (ProductVariant pv : entity.getProductVariants()) {
                this.image = pv.getImageUrl();
                this.price = pv.getPrice();
            }
        }
    }

}
