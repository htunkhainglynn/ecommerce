package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderItemDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer productVariantId;

    private String productName;

    private int quantity;

    private double price;

    private long orderId;

    private String imageUrl;

    private String size;

    private String color;


    public OrderItemDto(OrderItem orderItem) {
        this.price = orderItem.getProductVariant().getPrice();
        this.productName = orderItem.getProductVariant().getProduct().getName();
        this.imageUrl = orderItem.getProductVariant().getImageUrl();
        this.size = orderItem.getProductVariant().getSize();
        this.color = orderItem.getProductVariant().getColor();
        this.quantity = orderItem.getQuantity();
        this.orderId = orderItem.getOrder().getId();
    }

}
