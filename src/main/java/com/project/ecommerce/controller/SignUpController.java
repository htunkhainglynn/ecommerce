package com.project.ecommerce.controller;

import com.project.ecommerce.dto.SignUpDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    @PostMapping
    public void signUp(@RequestBody SignUpDto signUpDto) {

    }
}
