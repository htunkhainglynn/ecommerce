package com.project.ecommerce.repo;

import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.user.username = ?2")
    Optional<Order> findByIdAndUserUsername(Long id, String username);

    @Query("SELECT COUNT(o) FROM Order o")
    int findTotalOrders();

    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    double findTotalSales();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate = ?1")
    double findDailyRevenue(LocalDate today);

    @Query(value = "SELECT SUM(o.totalPrice) from Order o where o.orderDate between ?1 and ?2")
    double findRevenueBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o.orderItems FROM Order o WHERE o.orderDate = ?1")
    List<List<OrderItem>> findOrderItemsByOrderDate(LocalDate today);

    @Query("SELECT o.orderItems FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    List<List<OrderItem>> findOrderItemsBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o.orderItems FROM Order o WHERE o.id = ?1")
    List<OrderItem> getOrderItemsByOrderId(Long id);
}
