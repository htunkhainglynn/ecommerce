package com.project.ecommerce.controller;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@Api(value = "Sign Up Management")
public class SignUpController {

    private final UserService userService;

    @Autowired
    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Sign up", description = "Sign up with username, password, and email")
    public void signUp(@RequestBody SignUpDto signUpDto) {
        userService.saveUser(signUpDto);
    }
}
