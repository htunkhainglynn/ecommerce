package com.project.ecommerce.dto;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("ProductVariantCache")
@Data
public class ProductVariantCache implements Serializable {

    private Integer id;

    private String size;

    private String color;

    private int quantity;

    private double price;

    private Long product_id;
}
