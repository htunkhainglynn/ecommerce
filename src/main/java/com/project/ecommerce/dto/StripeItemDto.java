package com.project.ecommerce.dto;

import lombok.Data;

@Data
public class StripeItemDto {
    private String productName;

    private int quantity;

    private int price;
}
