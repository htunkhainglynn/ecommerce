package com.project.ecommerce.config;

import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.security.JwtTokenFilter;
import com.project.ecommerce.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain config(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http.cors(cors -> corsFilter());
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/auth/signin",
                                "/signup",
                                "/api/v1/products",
                                "/api/v1/reviews/**",
                                "/api/v1/product-variants",
                                "/api/v1/products/*/product-variants")
                        .permitAll()

                        .requestMatchers(
                                "/api/v1/product-variants/*/expense-history",
                                "/api/v1/roles/**",
                                "/api/v1/dashboard/**",
                                "/api/v1/products/*/availability",
                                "/api/v1/product-variants/expense-history/*")
                        .hasAuthority(Role.ADMIN.name())

                        .requestMatchers(
                                "/api/v1/orders/**",
                                "/api/v1/users/**",
                                "/api/v1/addresses/**",
                                "/api/v1/queue-info/**",
                                "/api/v1/notifications/**",
                                "/api/v1/profile/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())

                        .requestMatchers("/api/v1/stripe/**")
                        .hasAuthority(Role.USER.name())

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*"); // Allow all methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*"); // Allow all headers
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
