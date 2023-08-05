package com.project.ecommerce.utils;

import com.project.ecommerce.entitiy.*;
import com.project.ecommerce.repo.BrandRepository;
import com.project.ecommerce.repo.ColorRepository;
import com.project.ecommerce.repo.CustomerRepository;
import com.project.ecommerce.repo.SizeRepository;
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
    ColorRepository colorRepo;
    @Autowired
    SizeRepository sizeRepo;

    @Autowired
    BrandRepository brandRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

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

        List<String> colors = new ArrayList<>(List.of(
                "Red", "Green", "Blue", "Yellow", "Black", "White", "Orange", "Purple", "Brown", "Pink"
        ));
        colors.forEach(color -> {
            colorRepo.save(Color.builder()
                    .name(color)
                    .build());
        });

        List<String> sizes = new ArrayList<>(List.of(
                "XS", "S", "M", "L", "XL", "XXL", "5", "5.5",
                "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12"
        ));
        sizes.forEach(size -> {
            sizeRepo.save(Size.builder()
                    .name(size)
                    .build());
        });

        List<String> brands = new ArrayList<>(List.of(
                "Nike", "Adidas", "Puma", "Reebok", "Vans",
                "Converse", "New Balance", "Fila", "Under Armour", "Skechers",
                "Apple", "Samsung", "Huawei", "Xiaomi", "Oppo", "Vivo", "Realme", "OnePlus",
                "Sony", "LG", "Panasonic", "Toshiba", "Sharp", "Philips", "Hisense", "Skyworth",
                "Canon", "Nikon", "Sony", "Fujifilm", "Panasonic", "Olympus", "Leica", "GoPro",
                "Dell", "HP", "Lenovo", "Asus", "Acer", "Microsoft", "MSI", "Razer"
        ));
        brands.forEach(brand -> {
            brandRepo.save(Brand.builder()
                    .name(brand)
                    .build());
        });
    }
}
