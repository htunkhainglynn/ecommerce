package com.project.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePassword {

    @NotNull(message = "Old password is required")
    String oldPassword;

    @NotNull(message = "New password is required")
    String newPassword;
}
