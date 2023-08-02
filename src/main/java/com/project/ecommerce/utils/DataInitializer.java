package com.project.ecommerce.utils;

import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.Customer;
import com.project.ecommerce.repo.CustomerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class DataInitializer {

    @Autowired
    CustomerRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @PostConstruct
    public void initializeUser() {

        this.users.save(Customer.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.User))
                .build()
        );
        this.users.save(Customer.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.Admin))
                .build()
        );
    }
}
