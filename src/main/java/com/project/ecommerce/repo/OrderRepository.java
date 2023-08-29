package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Order;
import io.lettuce.core.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.user.username = ?2")
    Optional<Order> findByIdAndUserUsername(Long id, String username);
}
