package com.project.ecommerce.api;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@Api(value = "Sign Up Management")
public class SignUpApi {

    private final UserService userService;

    @Autowired
    public SignUpApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Sign up", description = "Sign up with username, password, and email")
    public void signUp(@RequestBody @Validated SignUpDto signUpDto) {
        userService.saveUser(signUpDto);
    }
}
