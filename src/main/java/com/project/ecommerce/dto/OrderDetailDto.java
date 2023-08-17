package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class OrderDetailDto {

    private Long orderId;

    private LocalDate orderDate;

    private double subTotal;

    private double totalPrice;

    private List<OrderItemDto> orderItems;

    private Status status;

    private UserDto user;

    public OrderDetailDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.subTotal = order.getSubTotal();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = new ArrayList<>();
        this.status = order.getStatus();
        this.user = new UserDto(order.getUser());
        order.getOrderItems().forEach(orderItem -> this.orderItems.add(new OrderItemDto(orderItem)));
    }

}
