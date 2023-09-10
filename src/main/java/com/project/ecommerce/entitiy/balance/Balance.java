package com.project.ecommerce.entitiy.balance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double income;
    private double expenses;
    private double profit;
    private String day;
    private String month;
    private Integer year;

    @Column(unique = true)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Type type;

}

