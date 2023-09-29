package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.entitiy.ProductVariant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotNull(message = "Price cannot be null")
    private double price;

    private boolean inStock;

    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    @NotNull(message = "Product id cannot be null")
    private Long productId  ;

    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Purchase price cannot be null")
    private double purchasePrice;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Image file cannot be null")
    private MultipartFile imageFile;

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
        this.productId = entity.getProduct().getId();
        this.inStock = entity.isInStock();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.expenses = entity.getExpenses();
        this.purchasePrice = entity.getPurchasePrice();
    }
}
