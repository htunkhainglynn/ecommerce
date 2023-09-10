package com.project.ecommerce.dto;

import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.entitiy.WishList;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
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

    private String profilePictureURL;

    private String phone;

    private String address;

    private boolean active;

    private List<OrderVo> orders = new ArrayList<>();

    private WishList wishList;

    List<Address> addresses;

    public UserDetailDto(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.addresses = user.getAddresses();
        this.active = user.isActive();
        user.getOrders().forEach(order -> this.orders.add(new OrderVo(order)));
        this.wishList = user.getWishList();
    }
}
