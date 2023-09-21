package com.project.ecommerce.controller;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.service.UserService;
import com.project.ecommerce.utils.PagerResult;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User Management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get all users
    @GetMapping
    @Operation(summary = "Get all users", description = "Requires ADMIN authority")
    public ResponseEntity<PagerResult<UserDto>> getAllUsers(@RequestParam(required = false) String keyword,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size) {
        Page<UserDto> result = userService.getAllUsers(keyword, page, size);
        PagerResult<UserDto> pagerResult = PagerResult.of(result);
        return ResponseEntity.ok(pagerResult);
    }

    // get user by id
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Requires ADMIN authority")
    public ResponseEntity<UserDetailDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // update user by id
    @PutMapping("/{id}")
    @Operation(summary = "Update user activation by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<UserDetailDto> updateUserActivationById(@PathVariable Long id) {
        Optional<UserDetailDto> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setActive(!user.get().isActive());
            return ResponseEntity.ok(userService.updateUser(user.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // delete user by id
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Requires ADMIN or USER authority")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        Optional<UserDetailDto> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
