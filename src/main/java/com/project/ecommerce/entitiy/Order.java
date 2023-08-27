package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Double.compare(getSubTotal(), order.getSubTotal()) == 0 && Double.compare(getTotalPrice(), order.getTotalPrice()) == 0 && Objects.equals(getId(), order.getId()) && Objects.equals(getOrderDate(), order.getOrderDate()) && Objects.equals(getUser(), order.getUser()) && Objects.equals(getOrderItems(), order.getOrderItems()) && Objects.equals(getShipping(), order.getShipping()) && getStatus() == order.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderDate(), getSubTotal(), getTotalPrice(), getUser(), getOrderItems(), getShipping(), getStatus());
    }
}
