package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDto saveOrder(OrderDto orderDto);

    List<OrderDto> getAllOrders();

    Optional<OrderDto> getOrderById(Long id);
}
