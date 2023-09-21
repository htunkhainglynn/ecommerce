package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    private String name;

    private String username;

    private String profilePictureURL;

    private String email;

    private String phone;

    private boolean active;

    private int orders;

    private List<Address> addresses;

    private double totalSpent;

    public UserDto(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.name = entity.getName();
        this.profilePictureURL = entity.getProfilePictureURL();
        this.email = entity.getEmail();
        this.phone = entity.getPhoneNumber();
        this.active = entity.isActive();
        this.orders = entity.getOrders().size();
        this.totalSpent = entity.getOrders().stream().mapToDouble(Order::getTotalPrice).sum();
    }
}
