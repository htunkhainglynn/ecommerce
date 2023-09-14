package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AuthenticationRequest;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.security.JwtTokenProvider;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository users;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository users) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.users = users;
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in", description = "Sign in with username or email and password")
    public ResponseEntity<Map<Object, Object>> signin(@RequestBody AuthenticationRequest data) {
        try {
            String usernameOrEmail = data.getUsername(); // Assuming the usernameOrEmail field contains either username or email
            String password = data.getPassword();

            // Check if the input is an email or username
            Optional<User> userReference = this.users.getReferenceByUsernameOrEmail(usernameOrEmail);
            if (userReference.isEmpty()) {
                throw new BadCredentialsException("Invalid username/email or password supplied");
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

}
