package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String size;

    private String color;

    @Column(columnDefinition = "int default 1")
    private int quantity;

    @Column(columnDefinition = "double default 0.0")
    private double price;

    @JsonIgnore
    @ManyToOne
    private Product product;

    @JsonIgnore
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<OrderItem> orderItem;

}
