package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.entitiy.WishList;
import com.project.ecommerce.vo.OrderVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDto {

    private Long id;

    private String name;

    private String username;

    private String email;

    private String profilePictureURL;

    private String phoneNumber;

    private String address;

    private boolean active;

    private List<OrderVo> orders = new ArrayList<>();

    private WishList wishList;

    private List<Address> addresses;

    // for unit test
    @JsonIgnore
    private String password;

    public UserDetailDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.addresses = user.getAddresses();
        this.active = user.isActive();
        user.getOrders().forEach(order -> this.orders.add(new OrderVo(order)));
        this.wishList = user.getWishList();
    }
}
