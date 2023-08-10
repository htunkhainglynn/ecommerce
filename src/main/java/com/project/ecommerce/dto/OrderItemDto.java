package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class OrderItemDto {

    private Integer product_id;

    private int quantity;
}
