package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDetailDto saveOrder(OrderDetailDto orderDetailDto);

    List<OrderDto> getAllOrders(String keyword, Optional<Integer> page, Optional<Integer> size);

    Optional<OrderDetailDto> getOrderById(Long id);
}
