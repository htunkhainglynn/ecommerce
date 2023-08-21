package com.project.ecommerce.utils;

import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.DynamicQueueManager;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DynamicQueueManager dynamicQueueManager;

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private FanoutExchange fanoutExchange;

    @Autowired
    private TopicExchange topicExchange;


    @Transactional
    @PostConstruct
    public void initializeUser() {

        this.userRepo.save(User.builder()
                .username("user")
                .email("user@gmail.com")
                .name("Ko User")
                .active(true)
                .password(this.passwordEncoder.encode("password"))
                .roles(List.of(Role.USER))
                .build()
        );
        dynamicQueueManager.createQueueForUser("user");

        this.userRepo.save(User.builder()
                .username("user2")
                .name("Ma User")
                .email("user2@gmail.com")
                .active(true)
                .password(this.passwordEncoder.encode("password"))
                .roles(List.of(Role.USER))
                .build()
        );
        dynamicQueueManager.createQueueForUser("user2");


        this.userRepo.save(User.builder()
                .username("admin")
                .name("Admin")
                .email("admin@gmail.com")
                .active(true)
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.ADMIN, Role.EDITOR))
                .build()
        );
        dynamicQueueManager.createQueueForUser("admin");


//        List<String> categories = new ArrayList<>(List.of(
//                "Electronics", "Fashion", "Home", "Appliances", "Toys", "Beauty", "Sports", "Automotive", "Other"
//        ));
//        categories.forEach(category -> {
//            categoryRepository.save(Category.builder()
//                    .name(category)
//                    .build());
//        });

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
