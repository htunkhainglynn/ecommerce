package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDetailVo saveOrder(OrderDetailDto orderDetailVo);

    Page<OrderVo> getAllOrders(String keyword, Optional<Integer> page, Optional<Integer> size);

    Optional<OrderDetailVo> getOrderById(Long id);

    OrderDetailVo updateStatus(Long id, Status status);

    Optional<OrderDetailVo> getOrderByIdWithUsername(Long id, String username);

    Optional<List<OrderItemDto>> getOrderItemsByOrderId(Long id);

    Page<OrderVo> getAllOrdersByUsername(String username, Optional<Integer> page, Optional<Integer> size);
}
