package com.project.ecommerce.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    private String street;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    private String city;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8_bin", unique = true)
    private String postalCode;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToOne(mappedBy = "address")
    private Order order;
}
