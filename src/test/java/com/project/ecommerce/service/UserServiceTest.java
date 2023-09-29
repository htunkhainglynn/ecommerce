package com.project.ecommerce.service;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql", "/sql/address.sql"})
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QueueInfoService queueInfoService;

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
        String queue = queueInfoService.getQueueNameByUsername(username);
        String key = queueInfoService.getRoutingKeyByUsername(username);

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
        assertEquals(queue, "ecommerce."+username+".queue");
        assertEquals(key, "ecommerce."+username+".key");
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

        assertThrows(UserException.class, () -> userService.saveUser(signUpDto));
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
        assertEquals(1, user.getAddresses().size());
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

    @Test
    @Order(8)
    @PreAuthorize("hasAuthority('ADMIN')")
    void testGetAllUsers() {

        setAuthentication("admin", Role.ADMIN);

        Page<UserDto> userDtoPage =  userService.getAllUsers(null, Optional.empty(), Optional.empty());

        assertEquals(3, userDtoPage.getTotalElements());
        assertEquals(1, userDtoPage.getTotalPages());
        assertEquals(3, userDtoPage.getContent().size());

        userDtoPage =  userService.getAllUsers("987-654-321", Optional.empty(), Optional.empty());

        assertEquals(2, userDtoPage.getTotalElements());
        assertEquals(2, userDtoPage.getContent().size());

        userDtoPage =  userService.getAllUsers("user1", Optional.empty(), Optional.empty());

        assertEquals(1, userDtoPage.getTotalElements());
        assertEquals(1, userDtoPage.getContent().size());
    }

    @Order(9)
    @Test
    void test_change_password() {
        setAuthentication("user1", Role.USER);
        Optional<User> user = userRepository.getReferenceByUsername("user1");
        assertTrue(user.isPresent());
        assertTrue(passwordEncoder.matches("password", user.get().getPassword()));

        userService.changePassword("password", "user1");

        user = userRepository.getReferenceByUsername("user1");
        assertTrue(user.isPresent());
        assertTrue(passwordEncoder.matches("user1", user.get().getPassword()));
    }

    @Order(10)
    @Test
    void test_change_password_same() {
        setAuthentication("user1", Role.USER);
        Optional<User> user = userRepository.getReferenceByUsername("user1");
        assertTrue(user.isPresent());
        assertTrue(passwordEncoder.matches("password", user.get().getPassword()));

        assertThrows(UserException.class, () -> userService.changePassword("password", "password"));
    }

    void setAuthentication(String username, Role role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(role::name));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
