package com.project.ecommerce.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.vo.OrderDetailVo;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    private String message;

    @Column(nullable = false)
    private LocalDate date;

    private OrderDetailVo order;

    @JsonIgnore
    private String username;
}
