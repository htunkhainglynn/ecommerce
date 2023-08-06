package com.project.ecommerce.utils;

import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.entitiy.Customer;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.repo.CategoryRepository;
import com.project.ecommerce.repo.CustomerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer {

    @Autowired
    CustomerRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void initializeUser() {

        this.userRepo.save(Customer.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.User))
                .build()
        );
        this.userRepo.save(Customer.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.Admin))
                .build()
        );

        List<String> categories = new ArrayList<>(List.of(
                "Electronics", "Fashion", "Home", "Appliances", "Toys", "Beauty", "Sports", "Automotive", "Other"
        ));
        categories.forEach(category -> {
            categoryRepository.save(Category.builder()
                    .name(category)
                    .build());
        });

//        List<String> colors = new ArrayList<>(List.of(
//                "Red", "Green", "Blue", "Yellow", "Black", "White", "Orange", "Purple", "Brown", "Pink"
//        ));
//        colors.forEach(color -> {
//            colorRepo.save(Color.builder()
//                    .name(color)
//                    .build());
//        });
//
//        List<String> sizes = new ArrayList<>(List.of(
//                "XS", "S", "M", "L", "XL", "XXL", "5", "5.5",
//                "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12"
//        ));
//        sizes.forEach(size -> {
//            sizeRepo.save(Size.builder()
//                    .name(size)
//                    .build());
//        });

    }
}
