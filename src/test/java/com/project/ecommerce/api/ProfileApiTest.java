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

import java.util.Map;

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
    void test_update_phone_number() {

        Map<String, String > phoneNumber = Map.of("phoneNumber", "081234567890");

        client.post().uri("/api/v1/profile/phone-number")
                .bodyValue(phoneNumber)
                .exchange()
                .expectStatus().isOk();

        UserDetailDto result = client.get().uri("/api/v1/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("081234567890", result.getPhoneNumber());
    }

    @Order(4)
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void test_delete_phone_number() {

        client.delete().uri("/api/v1/profile/phone-number")
                .exchange()
                .expectStatus().isOk();

        UserDetailDto result = client.get().uri("/api/v1/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDetailDto.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getPhoneNumber());
    }

}
