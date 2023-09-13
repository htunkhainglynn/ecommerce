package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    private int quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductVariant productVariant;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    public OrderItem(ProductVariant productVariant, int quantity, Order order) {
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.order = order;
    }
}
