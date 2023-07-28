package com.project.ecommerce.utils;

import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.repo.UserRepository;
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
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @PostConstruct
    public void initializeUser() {

        this.users.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.User))
                .build()
        );
        this.users.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.Admin, Role.User))
                .build()
        );

        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}
