package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ecommerce.entitiy.Role;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@Builder
public class SignUpDto {

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+$", message = "Invalid username")
    private String username;
    private String name;
    private String password;

    @Pattern(regexp = "^[0-9]{15}$", message = "Invalid phone number")
    private String phoneNumber;
    private String profilePictureURL;
    @JsonIgnore
    private List<Role> roles;
    
    @JsonIgnore
    private boolean active;
}
