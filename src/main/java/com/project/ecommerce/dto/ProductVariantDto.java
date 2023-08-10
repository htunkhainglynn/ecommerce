package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ProductVariantDto {

    private int id;

    private String size;

    private String color;

    private double price;

    private int quantity;

    private Long product_id;

    public ProductVariantDto(ProductVariant entity) {
        this.id = entity.getId();
        this.size = entity.getSize();
        this.color = entity.getColor();
        this.price = entity.getPrice();
        this.quantity = entity.getQuantity();
        this.product_id = entity.getProduct().getId();
    }

}
