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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique=true)
    private Integer sku;

    @NotNull
    private Double weight;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean available;

    @Column(columnDefinition = "boolean default true")
    private boolean inStock;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Organization organization; // Many products can belong to one category

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews;// One product can have multiple reviews

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;

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
