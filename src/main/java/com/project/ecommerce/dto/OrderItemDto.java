package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.entitiy.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class OrderItemDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer product_id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductVariantDto product;

    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.product = new ProductVariantDto(orderItem.getProductVariant());
        this.quantity = orderItem.getQuantity();
    }
}
