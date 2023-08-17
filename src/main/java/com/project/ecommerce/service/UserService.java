package com.project.ecommerce.service;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    Page<UserDto> getAllUsers(String keyword, Optional<Integer> page, Optional<Integer> size);

    Optional<UserDetailDto> getUserById(Long id);

    void saveUser(SignUpDto signUpDto);

    UserDetailDto updateUser(UserDetailDto userDetailDto);
}