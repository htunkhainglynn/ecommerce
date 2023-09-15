package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.vo.ProductVariantVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OrderItemDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer product_id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductVariantVo productVariant;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String productName;

    private int quantity;

    private int price;

    private long orderId;

    public OrderItemDto(OrderItem orderItem) {
        this.productVariant = new ProductVariantVo(orderItem.getProductVariant());
        this.quantity = orderItem.getQuantity();
        this.orderId = orderItem.getOrder().getId();
    }
}
