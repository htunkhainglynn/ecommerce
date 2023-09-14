package com.project.ecommerce.vo;

import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderDate;
    private String name;
    private Status status;
    private double totalPrice;

    public OrderVo(Order entity) {
        this.id = entity.getId();
        this.orderDate = entity.getOrderDate().toString();
        this.name = entity.getUser().getName();
        this.status = entity.getStatus();
        this.totalPrice = entity.getTotalPrice();
    }
}
