package com.project.ecommerce.api;

import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.utils.PagerResult;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql", "/sql/user.sql", "/sql/queue-info.sql", "/sql/address.sql", "/sql/orders.sql"})
public class OrderApiTest {

    private WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void test_get_all_orders() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        log.info("username: {}", username);
        log.info("authority: {}", authority);

        PagerResult<OrderVo> result = client.get().uri("/api/v1/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(3L));

        result = client.get().uri("/api/v1/orders?keyword=99.99")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(2L));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void test_get_all_orders_by_username() {
        PagerResult<OrderVo> result = client.get().uri("/api/v1/orders/user/user1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(2L));

        result = client.get().uri("/api/v1/orders/user/admin")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getData(), empty());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void test_get_order_by_id() {
        OrderDetailVo result = client.get().uri("/api/v1/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDetailVo.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getOrderId(), equalTo(1L));
        assertThat(result.getStatus(), equalTo(Status.PENDING));
        assertThat(result.getUser().getUsername(), equalTo("user1"));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "user", authorities = {"USER"})
    void test_get_order_items_by_order_id() {
        List<OrderItemDto> result = client.get().uri("/api/v1/orders/order-items/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderItemDto.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user", authorities = {"USER"})
    void test_update_order_status() {
        OrderDetailVo result = client.put().uri("/api/v1/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDetailVo.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(Status.SHIPPED));
    }
}
