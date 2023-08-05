package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int stock;
    @ManyToMany
    @JoinTable(
            name = "product_size",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id")

    )
    private List<Size> sizes;
    @ManyToMany
    @JoinTable(
            name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private List<Color> colors;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id") // Foreign key column in the Product table
    private Category category; // Many products can belong to one category

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews;// One product can have multiple reviews

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
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Transient
    private double averageRating;  // for json return

    @PostLoad
    private void calculateAverageRating() {
        if (reviews != null && !reviews.isEmpty()) {
            averageRating = reviews.stream().mapToDouble(Review::getRating).sum() / reviews.size();
        } else {
            averageRating = 0.0; // Or any other default value when no reviews are available
        }
    }
}
