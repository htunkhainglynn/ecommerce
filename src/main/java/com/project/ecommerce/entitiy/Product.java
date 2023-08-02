package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id") // Foreign key column in the Product table
    private Category category; // Many products can belong to one category

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews;// One product can have multiple reviews
    @OneToMany(mappedBy = "product",
              cascade = CascadeType.ALL,
              fetch = FetchType.LAZY
    )
    private List<Cart> cart;

    @JsonIgnore
    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<OrderItem> orderItem;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;
}
