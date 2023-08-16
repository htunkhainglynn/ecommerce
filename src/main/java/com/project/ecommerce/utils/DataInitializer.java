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

    private final DirectExchange directExchange;
    private final FanoutExchange fanoutExchange;
    private final TopicExchange topicExchange;

    private Exchange[] exchanges;

    public DataInitializer(DirectExchange directExchange, FanoutExchange fanoutExchange, TopicExchange topicExchange) {
        this.directExchange = directExchange;
        this.fanoutExchange = fanoutExchange;
        this.topicExchange = topicExchange;

        this.exchanges = new Exchange[] {directExchange, fanoutExchange, topicExchange};
    }

    @Transactional
    @PostConstruct
    public void initializeUser() {

        this.userRepo.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.User))
                .build()
        );
        dynamicQueueManager.createQueueForUser("user");


        this.userRepo.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.Admin, Role.Editor))
                .build()
        );
        dynamicQueueManager.createQueueForUser("admin");

        this.userRepo.save(User.builder()
                .username("editor")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList(Role.Editor))
                .build()
        );
        dynamicQueueManager.createQueueForUser("editor");

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
