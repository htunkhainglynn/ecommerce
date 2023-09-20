package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Status status;

    private double totalPrice;

    private Long addressId;

    private AddressDto address;

    private List<OrderItemDto> orderItems;
}
