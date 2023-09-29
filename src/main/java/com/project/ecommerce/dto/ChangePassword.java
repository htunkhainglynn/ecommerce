package com.project.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePassword {

    @NotNull(message = "Old password is required")
    String oldPassword;

    @NotNull(message = "New password is required")
    String newPassword;
}
