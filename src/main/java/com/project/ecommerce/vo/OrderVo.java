package com.project.ecommerce.vo;

import com.project.ecommerce.entitiy.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OrderVo {

    private Long id;
    private String orderDate;
    private String name;
    private double totalPrice;

    public OrderVo(Order entity) {
        this.id = entity.getId();
        this.orderDate = entity.getOrderDate().toString();
        this.name = entity.getUser().getName();
        log.info("name: {}", this.name);
        this.totalPrice = entity.getTotalPrice();
    }
}
