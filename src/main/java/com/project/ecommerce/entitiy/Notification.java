package com.project.ecommerce.entitiy;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Notification")
@Data
public class Notification {

    private Long id;

    private String message;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;
}
