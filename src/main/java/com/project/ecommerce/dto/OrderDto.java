package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String orderDate;
    private String name;
    private double totalPrice;
    private Status status;

    public OrderDto(Order entity) {
        this.id = entity.getId();
        this.orderDate = entity.getOrderDate().toString();
        this.name = entity.getUser().getName();
        this.totalPrice = entity.getTotalPrice();
        this.status = entity.getStatus();
    }
}
