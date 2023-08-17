package com.project.ecommerce.controller;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping
    public Page<UserDto> getAllUsers(@RequestParam(required = false) String keyword,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size) {
        return userService.getAllUsers(keyword, page, size);
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // update user by id
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDto> updateUserById(@PathVariable Long id,
                                                        @RequestBody UserDetailDto userDetailDto) {
        Optional<UserDetailDto> user = userService.getUserById(id);
        if (user.isPresent()) {
            userDetailDto.setId(id);
            return ResponseEntity.ok(userService.updateUser(userDetailDto));
        }
        return ResponseEntity.notFound().build();
    }

}
