package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private double subTotal;

    private double totalPrice;

    private List<OrderItemDto> orderItems;
}
