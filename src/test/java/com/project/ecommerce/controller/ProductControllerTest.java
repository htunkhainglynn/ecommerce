package com.project.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Organization;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.utils.PagerResult;
import com.project.ecommerce.vo.ProductVariantVo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql"})
public class ProductControllerTest {

    private WebTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = MockMvcWebTestClient.bindToApplicationContext(context).build();
    }

    void setAuthentication(String username, Role role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(role::name));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Order(1)
    @Test
    void test_get_all_products() {
        PagerResult<ProductDto> result = client.get().uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(2L));

        result = client.get().uri("/api/v1/products?keyword=Product 1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(1L));

        result = client.get().uri("/api/v1/products?isAvailable=true")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PagerResult.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.getPager().getTotalElements(), equalTo(2L));
    }

    @Order(2)
    @Test
    void test_get_product_variant_by_id() {
        List<ProductVariantVo> result = client.get().uri("/api/v1/products/1/product-variants")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductVariantVo.class)
                .returnResult().getResponseBody();

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0).getPrice(), equalTo(29.99));
    }

//    @Order(3)
//    @ParameterizedTest
//    @CsvFileSource(resources = "/csv/product/create.txt", numLinesToSkip = 1)
//    @PreAuthorize("hasAuthority('ADMIN')")
//    void test_create_product(
//            Integer sku, Double weight, String name, String description, Boolean available,
//            String size1, String color1, Double price1, Boolean inStock1, Integer quantity1,
//            Long productId1, Double purchasePrice1, String imageUrl1, String size2, String color2,
//            Double price2, Boolean inStock2, Integer quantity2, Long productId2, Double purchasePrice2,
//            String imageUrl2, String vendor, String category
//    ) throws JsonProcessingException {
//        setAuthentication("admin", Role.ADMIN);
//
//        MockMultipartFile file1 = new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "file1".getBytes());
//
//        MultipartFile file2 = new MockMultipartFile("file2", "file2.jpg", "image/jpeg", "file2".getBytes());
//
//        ProductVariantDto pv1 = ProductVariantDto.builder().size(size1).color(color1)
//                .price(price1).inStock(inStock1).quantity(quantity1).purchasePrice(purchasePrice1)
//                .imageFile(file1).build();
//
//        ProductVariantDto pv2 = ProductVariantDto.builder().size(size2).color(color2)
//                .price(price2).inStock(inStock2).quantity(quantity2).purchasePrice(purchasePrice2)
//                .imageFile(file2).build();
//
//        List<ProductVariantDto> pvList = List.of(pv1, pv2);
//
//        Organization organization = Organization.builder().vendor(vendor).category(category).build();
//
//        ProductDto product = ProductDto.builder().sku(sku).weight(weight).name(name).description(description)
//                .available(available).productVariants(pvList).organization(organization).build();
//
//        // Serialize ProductDto to JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//        String productJson = objectMapper.writeValueAsString(product);
//
//        // Create a MultiValueMap for parts
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//        parts.add("product", new ByteArrayResource(productJson.getBytes()) {
//            @Override
//            public String getFilename() {
//                return "product.json";
//            }
//        });
//
//        // Create the WebClient request
//        client.post()
//                .uri("/api/v1/products")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(parts))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(ProductDto.class);
//
//    }

    @Order(4)
    @Test
    void test_update_product_availability() {
        setAuthentication("admin", Role.ADMIN);
        client.put().uri("/api/v1/products/1/availability").exchange().expectStatus().isOk();
    }

//    @Order(5)
//    @Test
//    void test_delete_product() {
//        setAuthentication("admin", Role.ADMIN);
//        client.delete().uri("/api/v1/products/1").exchange().expectStatus().isOk();
//
//        client.get().uri("/api/v1/products/1").exchange().expectStatus().is4xxClientError();
//    }

}
