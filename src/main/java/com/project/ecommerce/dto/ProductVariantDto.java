package com.project.ecommerce.dto;

import lombok.Data;

@Data
public class ProductVariantDto {

    private String size;

    private String color;

    private double price;

    private int quantity;

    private Long product_id;

}
