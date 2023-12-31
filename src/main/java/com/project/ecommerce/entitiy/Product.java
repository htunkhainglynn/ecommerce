package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.dto.ProductDto;
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

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "boolean default false")
    private boolean available;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ProductVariant> productVariants;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Organization organization; // Many products can belong to one category

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews;// One product can have multiple reviews

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WishList> wishLists;

    @Transient
    private double averageRating;  // for json return

    public Product(ProductDto dto) {
        this.id = dto.getId();
        this.sku = dto.getSku();
        this.weight = dto.getWeight();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.available = dto.isAvailable();
    }

    @PostLoad
    private void calculateAverageRating() {
        if (reviews != null && !reviews.isEmpty()) {
            averageRating = reviews.stream().mapToDouble(Review::getRating).sum() / reviews.size();
        } else {
            averageRating = 0.0; // Or any other default value when no reviews are available
        }
    }
}
