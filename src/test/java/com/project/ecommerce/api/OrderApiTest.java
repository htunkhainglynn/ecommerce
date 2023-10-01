package com.project.ecommerce.api;

import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.utils.PagerResult;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql", "/sql/user.sql", "/sql/queue-info.sql", "/sql/address.sql", "/sql/orders.sql"})
public class OrderApiTest {

    private WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    void setAuthentication(String username, Role role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(role::name));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    @Test
//    @Order(1)
//    public void test_get_all_orders() {
//        setAuthentication("admin", Role.ADMIN);
//        PagerResult<OrderVo> result = client.get().uri("/api/v1/orders")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PagerResult.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result, notNullValue());
//        assertThat(result.getPager().getTotalElements(), equalTo(3L));
//
//        result = client.get().uri("/api/v1/orders?keyword=99.99")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PagerResult.class)
//                .returnResult().getResponseBody();
//
//        assertThat(result, notNullValue());
//        assertThat(result.getPager().getTotalElements(), equalTo(2L));
//    }

    @Test
    @Order(2)
    public void test_get_all_orders_by_username() {
        setAuthentication("user", Role.USER);
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
    public void test_get_order_by_id() {
        setAuthentication("user1", Role.USER);
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
    void test_get_order_items_by_order_id() {
        setAuthentication("user1", Role.USER);
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
    void test_update_order_status() {
        setAuthentication("user1", Role.USER);
        OrderDetailVo result = client.put().uri("/api/v1/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderDetailVo.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getStatus(), equalTo(Status.SHIPPED));
    }
}
