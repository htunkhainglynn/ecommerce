package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Shipping;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.entitiy.WishList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private boolean active;

    private List<OrderDetailDto> orders = new ArrayList<>();

    private List<Shipping> shipping = new ArrayList<>();

    private WishList wishList;

    public UserDetailDto(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.address = user.getAddress();
        this.active = user.isActive();
        user.getOrders().forEach(order -> this.orders.add(new OrderDetailDto(order)));
        this.shipping.addAll(user.getShipping());
        this.wishList = user.getWishList();
    }
}
