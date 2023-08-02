package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDto {

    private int id;
    private int quantity;
    private Long product_id;
    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.product_id = cart.getProduct().getId();
    }
}
