package com.project.ecommerce.api;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.utils.PagerResult;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "admin", authorities = {"ADMIN"})
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql"})
public class UserApiTest {

    WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    @Order(1)
    void test_get_all_users() {
        PagerResult<UserDto> result = client.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertEquals(3, result.getPager().getTotalElements());

        result = client.get().uri("/api/v1/users?keyword=user1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertEquals(1, result.getPager().getTotalElements());
    }

    @Test
    @Order(2)
    void test_get_user_by_id() {
        UserDetailDto result = client.get().uri("/api/v1/users/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    @Order(3)
    void test_update_user_activation_by_id() {
        UserDetailDto result = client.get().uri("/api/v1/users/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertFalse(result.isActive());

        result = client.put().uri("/api/v1/users/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        assertTrue(result.isActive());
    }

    @Test
    @Order(4)
    void test_delete_user_by_id() {
        List<UserDto> result = client.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody().getData();

        assertNotNull(result);
        assertEquals(3, result.size());

        client.delete().uri("/api/v1/users/2")
                .exchange()
                .expectStatus().isOk();

        result = client.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody().getData();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
