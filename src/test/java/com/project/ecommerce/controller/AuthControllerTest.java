package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AuthenticationRequest;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql"})
public class AuthControllerTest {

    private WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    @Order(1)
    void test_sign_in() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("admin")
                .password("password")
                .build();

        Map<Object, Object> result = client.post().uri("/auth/signin")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class).returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.get("username"), is("admin"));
    }

    @Test
    @Order(2)
    void test_sign_in_not_active_account() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("user1")
                .password("password")
                .build();

        Map<Object, Object> result = client.post().uri("/auth/signin")
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(Map.class).returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.get("message"), equalTo("User is not active"));
    }

    @Test
    @Order(3)
    void test_sign_in_invalid_username() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("userxxx")
                .password("password")
                .build();

        Map<Object, Object> result = client.post().uri("/auth/signin")
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(Map.class).returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.get("message"), equalTo("Invalid username/email or password supplied"));
    }

    @Test
    @Order(4)
    void test_sign_in_invalid_password() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("admin")
                .password("passwordxxx")
                .build();

        Map<Object, Object> result = client.post().uri("/auth/signin")
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(Map.class).returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.get("message"), equalTo("Invalid username/email or password supplied"));
    }
}
