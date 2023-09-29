package com.project.ecommerce.api;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.service.CloudinaryService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.UserService;
import com.project.ecommerce.vo.OrderDetailVo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@RequestMapping("/api/v1/profile")
@Api(value = "Profile Management")
public class ProfileApi {

    private final CloudinaryService cloudinaryService;

    private final UserService userService;

    private final OrderService orderService;

    @Autowired
    public ProfileApi(CloudinaryService cloudinaryService,
                      UserService userService,
                      OrderService orderService) {
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get profile", description = "Requires USER authority")
    public ResponseEntity<UserDetailDto> getProfile() {
        Optional<UserDetailDto> userDetail = getUserDetailDto();
        return userDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Get order purchased by user with id", description = "Requires USER authority")
    public ResponseEntity<OrderDetailVo> getUserOrderById(@PathVariable("id") Long id) {
        Optional<OrderDetailVo> orderDetail = orderService.getOrderById(id);
        return orderDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload profile picture", description = "Requires USER authority")
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
    @Operation(summary = "Delete profile picture", description = "Requires USER authority")
    public void deleteProfilePic() {
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
