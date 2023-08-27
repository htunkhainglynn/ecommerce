package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String username;

    private String profilePictureURL;

    private String email;

    private String phone;

    private boolean active;

    private int orders;

    private long totalSpent;

    public UserDto(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.name = entity.getName();
        this.profilePictureURL = entity.getProfilePictureURL();
        this.email = entity.getEmail();
        this.phone = entity.getPhoneNumber();
        this.active = entity.isActive();
        this.orders = entity.getOrders().size();
        this.totalSpent = entity.getOrders().stream().mapToLong(order -> (long) order.getTotalPrice()).sum();
    }
}
