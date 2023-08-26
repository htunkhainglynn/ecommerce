package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
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
        log.info("name: {}", this.name);
        this.totalPrice = entity.getTotalPrice();
        this.status = entity.getStatus();
    }
}
