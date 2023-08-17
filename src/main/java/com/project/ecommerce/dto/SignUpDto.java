package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SignUpDto {

    private String email;
    private String username;
    private String name;
    private String password;
    
    @JsonIgnore
    private boolean active;
}
