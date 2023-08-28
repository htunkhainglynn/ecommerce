package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {
    OrderDetailVo saveOrder(OrderDetailDto orderDetailVo);

    Page<OrderVo> getAllOrders(String keyword, Optional<Integer> page, Optional<Integer> size);

    Optional<OrderDetailVo> getOrderById(Long id);
}
