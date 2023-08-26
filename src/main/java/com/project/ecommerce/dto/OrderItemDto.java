package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.vo.ProductVariantVo;
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
    private ProductVariantVo product;

    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.product = new ProductVariantVo(orderItem.getProductVariant());
        this.quantity = orderItem.getQuantity();
    }
}
