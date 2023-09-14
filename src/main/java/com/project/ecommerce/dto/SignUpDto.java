package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.entitiy.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SignUpDto {

    private String email;
    private String username;
    private String name;
    private String password;
    private String phoneNumber;
    private String profilePictureURL;
    @JsonIgnore
    private List<Role> roles;
    
    @JsonIgnore
    private boolean active;
}
