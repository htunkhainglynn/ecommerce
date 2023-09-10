package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String size;

    private String color;

    @Column(columnDefinition = "int default 1")
    private int quantity;

    @Column(columnDefinition = "double default 0.0")
    private double price;

    @Column(columnDefinition = "double default 0.0")
    private double purchasePrice;

    private String imageUrl;

    @Column(columnDefinition = "boolean default false")
    private boolean inStock;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @JsonIgnore
    @ManyToOne
    private Product product;

    @JsonIgnore
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private List<OrderItem> orderItem;

    @JsonIgnore
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private List<Expense> expenses;
}
