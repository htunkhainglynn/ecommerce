package com.project.ecommerce.vo;

import com.project.ecommerce.entitiy.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantVo {

    private int id;

    private String size;

    private String color;

    private double price;

    private String imageUrl;

    private String name;

    private int quantity;

    private Long productId;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private boolean inStock;

    private double purchasePrice;

    public ProductVariantVo(ProductVariant entity) {
        this.id = entity.getId();
        this.size = entity.getSize();
        this.color = entity.getColor();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.name = entity.getProduct().getName();
        this.productId = entity.getProduct().getId();
        this.inStock = entity.isInStock();
        this.quantity = entity.getQuantity();
        this.purchasePrice = entity.getPurchasePrice();
    }
}
