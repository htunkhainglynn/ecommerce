package com.project.ecommerce.controller;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProfileService;
import com.project.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    private final CloudinaryService cloudinaryService;

    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService,
                             CloudinaryService cloudinaryService,
                             UserService userService) {
        this.profileService = profileService;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
    }

    @GetMapping
    public void getProfile() {
        // get user id from security context
        // get user by id
        // return user
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        // get user id from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // get user by id
        Optional<UserDetailDto> user = userService.getUserByUsername(username);

        String imageUrl = "";
        // upload profile picture
        if (user.isPresent()) {
            imageUrl = cloudinaryService.uploadFile("profile", file);
            user.get().setProfilePictureURL(imageUrl);
            userService.updateUser(user.get());
        }
        return ok(imageUrl);
    }

    @PutMapping
    public ResponseEntity<UserDetailDto> updateProfile(@RequestBody UserDetailDto userDetailDto) {
        // get user id from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // get user by id
        Optional<UserDetailDto> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            userDetailDto.setId(user.get().getId());
            return ok(userService.updateUser(userDetailDto));
        }
        return ResponseEntity.notFound().build();
    }
}
