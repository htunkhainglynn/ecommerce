package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.entitiy.ProductVariant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDto {

    private int id;

    private String size;

    private String color;

    private double price;

    private boolean inStock;

    private int quantity;

    private Long product_id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private double purchasePrice;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile imageFile;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Expense> expenses;

    public ProductVariantDto(ProductVariant entity) {
        this.id = entity.getId();
        this.size = entity.getSize();
        this.color = entity.getColor();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        this.quantity = entity.getQuantity();
        this.name = entity.getProduct().getName();
        this.product_id = entity.getProduct().getId();
        this.inStock = entity.isInStock();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.expenses = entity.getExpenses();
    }
}
