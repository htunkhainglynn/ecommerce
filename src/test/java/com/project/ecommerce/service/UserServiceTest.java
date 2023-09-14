package com.project.ecommerce.service;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.entitiy.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql"})
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/user/creation.txt", delimiter = ',')
    void testCreate(boolean active, String email, String name, String password, String phoneNumber, String profilePictureURL, String username, int id) {
        SignUpDto signUpDto = SignUpDto.builder()
                .active(active)
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .profilePictureURL(profilePictureURL)
                .username(username)
                .build();

        Long beforeSave = userService.getRoleCount();
        User user = userService.saveUser(signUpDto);
        Long afterSave = userService.getRoleCount();

        // check result
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(active, user.isActive());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(profilePictureURL, user.getProfilePictureURL());
        assertEquals(username, user.getUsername());
        assertTrue(beforeSave < afterSave);
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/user/creation-duplicate.txt", delimiter = ',')
    void testCreateDuplicate(boolean active, String email, String name, String password, String phoneNumber, String profilePictureURL, String username) {
        SignUpDto signUpDto = SignUpDto.builder()
                .active(active)
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .profilePictureURL(profilePictureURL)
                .username(username)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.saveUser(signUpDto));
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/user/find-one.txt", delimiter = ',')
    void testFindOne(String email, String name, String password, String phoneNumber, String profilePictureURL, String username, boolean active, int id) {
        Optional<UserDetailDto> user = userService.getUserByUsername(username);

        // check result
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(id, user.get().getId());
        assertEquals(active, user.get().isActive());
        assertEquals(email, user.get().getEmail());
        assertEquals(username, user.get().getUsername());
        assertEquals(name, user.get().getName());
        assertEquals(phoneNumber, user.get().getPhoneNumber());
        assertTrue(passwordEncoder.matches(password, user.get().getPassword()));
        assertEquals(profilePictureURL, user.get().getProfilePictureURL());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/user/find-one-with-null.txt", delimiter = ',')
    @Order(4)
    void testFindOneWithNull(String email, String name, String password, String username, boolean active, int id) {
        Optional<UserDetailDto> user = userService.getUserByUsername(username);

        // check result
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(id, user.get().getId());
        assertEquals(active, user.get().isActive());
        assertEquals(email, user.get().getEmail());
        assertEquals(username, user.get().getUsername());
        assertEquals(name, user.get().getName());
        assertTrue(passwordEncoder.matches(password, user.get().getPassword()));
        assertNull(user.get().getProfilePictureURL());
        assertNull(user.get().getPhoneNumber());
    }

    @Order(5)
    @Test
    void testNotFound() {
        Optional<UserDetailDto> user = userService.getUserByUsername("notfound");

        // check result
        assertNotNull(user);
        assertFalse(user.isPresent());
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/user/update.txt", delimiter = ',')
    void testUpdate(String email, String name, String password, String phoneNumber, String profilePictureURL, String username, boolean active, long id) {
        UserDetailDto userDetailDto = UserDetailDto.builder()
                .active(active)
                .email(email)
                .name(name)
                .username(username)
                .password(password)
                .phoneNumber(phoneNumber)
                .profilePictureURL(profilePictureURL)
                .id(id)
                .build();

        UserDetailDto user = userService.updateUser(userDetailDto);

        // check result
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(active, user.isActive());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
        assertEquals(profilePictureURL, user.getProfilePictureURL());
    }

    @Test
    @Order(7)
    void testDelete() {
        Long beforeDelete = userService.getRoleCount();
        userService.deleteUserById(1L);
        Long afterDelete = userService.getRoleCount();
        Optional<UserDetailDto> user = userService.getUserById(1L);
        assertTrue(user.isEmpty());
        assertTrue(beforeDelete > afterDelete);
    }

}
