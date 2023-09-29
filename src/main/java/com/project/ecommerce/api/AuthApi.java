package com.project.ecommerce.api;

import com.project.ecommerce.dto.AuthenticationRequest;
import com.project.ecommerce.dto.ChangePassword;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.security.JwtTokenProvider;
import com.project.ecommerce.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@Api(value = "Authentication Management")
public class AuthApi {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public AuthApi(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository users, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = users;
        this.userService = userService;
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in", description = "Sign in with username or email and password")
    public ResponseEntity<Map<Object, Object>> signin(@RequestBody AuthenticationRequest data) {
        try {
            String usernameOrEmail = data.getUsername(); // Assuming the usernameOrEmail field contains either username or email
            String password = data.getPassword();

            // Check if the input is an email or username
            Optional<User> userReference = this.userRepository.getReferenceByUsernameOrEmail(usernameOrEmail);
            if (userReference.isEmpty()) {
                throw new BadCredentialsException("Invalid username/email or password supplied");
            }

            if (!userReference.get().isActive()) {
                throw new UserException("User is not active");
            }

            String username = userReference.get().getUsername();

            // Authenticate with either username or email
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String token = jwtTokenProvider.createToken(username, userReference
                    .get()
                    .getRoles()
                    .stream()
                    .map(Role::name)
                    .toList());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/email or password supplied");
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change Password", description = "Need Admin or User authority")
    void changePassword(@RequestBody ChangePassword changePassword) {
        userService.changePassword(changePassword.getOldPassword(), changePassword.getNewPassword());
    }

}
