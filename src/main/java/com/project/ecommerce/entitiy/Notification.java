package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.dto.OrderDetailDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notification")
@Data
@Builder
public class Notification {

    @Id
    private String id;

    private String message;

    private OrderDetailDto order;

    @JsonIgnore
    private String username;
}
