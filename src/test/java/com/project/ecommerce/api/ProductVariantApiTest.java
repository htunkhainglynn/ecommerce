package com.project.ecommerce.api;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.dto.ProductVariantDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql"})
public class ProductVariantApiTest {

    private WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void test_get_product_variant_by_id() {
        ProductVariantDto result = client.get().uri("/api/v1/product-variants/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductVariantDto.class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Medium", result.getSize());
        Assertions.assertEquals("Red", result.getColor());
        Assertions.assertEquals(29.99, result.getPrice());
        Assertions.assertEquals(19.99, result.getPurchasePrice());
        Assertions.assertEquals(1, result.getProductId());
    }

    @Order(2)
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void get_expense_history() {
        List<ExpenseDto> result = client.get().uri("/api/v1/product-variants/1/expense-history")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExpenseDto.class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getProductVariantId());
        Assertions.assertEquals(19.99, result.get(0).getPurchasePrice());
    }

    @Order(3)
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void update_expense_history() {
        ExpenseDto expenseDto = ExpenseDto.builder()
                .purchasePrice(20.00)
                .quantity(2)
                .build();

        client.put().uri("/api/v1/product-variants/expense-history/1")
                .bodyValue(expenseDto)
                .exchange()
                .expectStatus().isOk();

        List<ExpenseDto> result = client.get().uri("/api/v1/product-variants/1/expense-history")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExpenseDto.class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getProductVariantId());
        Assertions.assertEquals(20.00, result.get(0).getPurchasePrice());
        Assertions.assertEquals(2, result.get(0).getQuantity());
        Assertions.assertEquals(40.00, result.get(0).getTotal());
    }

}
