package com.project.ecommerce.api;

import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.vo.OrderDetailVo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql", "/sql/address.sql", "/sql/product.sql", "/sql/orders.sql"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileApiTest {

    private WebTestClient client;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    @Order(1)
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void test_get_user_detail() {
        UserDetailDto result = client.get().uri("/api/v1/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("user1", result.getUsername());
    }

    @Order(2)
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void test_get_user_order_by_id() {
        OrderDetailVo result = client.get().uri("/api/v1/profile/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDetailVo.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("user1", result.getUser().getUsername());
    }

    @Order(3)
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void test_upload_profile_picture() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        try {
            mockMvc.perform(multipart("/api/v1/profile/upload").file(file))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserDetailDto result = client.get().uri("/api/v1/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProfilePictureURL());
    }
}
