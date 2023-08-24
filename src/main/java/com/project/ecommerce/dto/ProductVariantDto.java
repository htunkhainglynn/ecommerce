package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.ecommerce.entitiy.ProductVariant;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDto {

    private int id;

    private String size;

    private String color;

    private double price;

    private int quantity;

    @JsonIgnore
    private MultipartFile imageFile;

    private String imageUrl;

    private String name;

    private Long product_id;

    public ProductVariantDto(ProductVariant entity) {
        this.id = entity.getId();
        this.size = entity.getSize();
        this.color = entity.getColor();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        this.quantity = entity.getQuantity();
        this.name = entity.getProduct().getName();
        this.product_id = entity.getProduct().getId();
    }
}
