package com.project.ecommerce.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "total_price")
    private double totalPrice;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id", insertable=false, updatable=false)
    private Order order;
}
