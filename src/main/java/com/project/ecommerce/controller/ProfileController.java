package com.project.ecommerce.controller;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.ProfileService;
import com.project.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        Optional<UserDetailDto> user = getUserDetailDto();

        String imageUrl = "";
        // upload profile picture
        if (user.isPresent()) {

            // delete image if there is already profile picture
            if (user.get().getProfilePictureURL() != null) {
                cloudinaryService.deleteImage(user.get().getProfilePictureURL());
            }

            imageUrl = cloudinaryService.uploadFile("profile", file);
            user.get().setProfilePictureURL(imageUrl);
            updateProfile(user.get());
        }
        return ok(imageUrl);
    }

    @PutMapping("/delete-picture")
    public void deleteProfile() {
        Optional<UserDetailDto> user = getUserDetailDto();

        if (user.isPresent()) {
            // delete profile picture
            if (user.get().getProfilePictureURL() != null) {
                cloudinaryService.deleteImage(user.get().getProfilePictureURL());
            }

            // set profile picture url to null
            user.get().setProfilePictureURL(null);
            updateProfile(user.get());
        }
    }

    private Optional<UserDetailDto> getUserDetailDto() {
        // get user id from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // get user by id
        return userService.getUserByUsername(username);
    }

    private void updateProfile(UserDetailDto userDetailDto) {
        Optional<UserDetailDto> user = getUserDetailDto();

        if (user.isPresent()) {
            userDetailDto.setId(user.get().getId());
            userService.updateUser(userDetailDto);
        }
    }
}
