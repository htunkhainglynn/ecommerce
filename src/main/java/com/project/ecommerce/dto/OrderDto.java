package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class OrderDto {

    private Long orderId;

    private LocalDate orderDate;

    private double subTotal;

    private double totalPrice;

    private List<OrderItemDto> orderItems;

    private Status status;

    private Long user_id;

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.subTotal = order.getSubTotal();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = new ArrayList<>();
        this.status = order.getStatus();
        this.user_id = order.getUser().getId();
        for(OrderItem orderItem : order.getOrderItems()) {
            this.orderItems.add(new OrderItemDto(orderItem));
        }
    }

}
