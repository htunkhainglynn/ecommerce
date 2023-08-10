package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
