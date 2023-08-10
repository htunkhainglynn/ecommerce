package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderDto saveOrder(OrderDto orderDto);

    List<OrderDto> getAllOrders();
}
