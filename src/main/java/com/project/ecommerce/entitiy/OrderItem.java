package com.project.ecommerce.entitiy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(ProductVariant productVariant, int quantity, Order order) {
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.order = order;
    }
}
