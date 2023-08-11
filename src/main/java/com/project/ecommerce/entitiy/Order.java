package com.project.ecommerce.entitiy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "order_date")
    private LocalDate orderDate;

    @NotNull
    @Column(name = "sub_total")
    private double subTotal;

    @NotNull
    @Column(name = "total_price")
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    @Column(columnDefinition = "boolean default false")
    private boolean status;
}
