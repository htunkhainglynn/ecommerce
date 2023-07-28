package com.project.ecommerce.security;

import com.project.ecommerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findOneByUsername(username)
                .map(user -> User.withUsername(username)
                        .password(user.getPassword())
                        .authorities(user.getAuthorities())
                        .disabled(!user.isEnabled())
                        .accountExpired(!user.isAccountNonExpired())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

    }


}