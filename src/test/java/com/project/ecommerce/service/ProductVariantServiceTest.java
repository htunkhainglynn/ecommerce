package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.vo.ProductVariantVo;
import lombok.With;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql"})
public class ProductVariantServiceTest {

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductService productService;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/productVariant/create.txt")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveProductVariant(String size, String color, double price, boolean inStock,
                                int quantity, Long productId, String imageUrl, double purchasePrice) {
        ProductVariantDto productVariantDto = ProductVariantDto.builder()
                                                        .size(size)
                                                        .color(color)
                                                        .price(price)
                                                        .inStock(inStock)
                                                        .quantity(quantity)
                                                        .product_id(productId)
                                                        .imageUrl(imageUrl)
                                                        .purchasePrice(purchasePrice)
                                                        .build();

        // pre check
        ProductDto productDto = productService.getProductById(productId).orElseThrow();
        int oldPvQuantity = productDto.getProductVariants().size();

        ProductVariantVo productVariantVo = productVariantService.saveProductVariant(productVariantDto);

        // post check
        productDto = productService.getProductById(productId).orElseThrow();
        int newPvQuantity = productDto.getProductVariants().size();

        assertThat(newPvQuantity, equalTo(oldPvQuantity + 1));
        assertThat(productVariantVo, notNullValue());
        assertThat(productVariantVo.getId(), notNullValue());
        assertThat(productVariantVo.getSize(), equalTo(size));
        assertThat(productVariantVo.getColor(), equalTo(color));
        assertThat(productVariantVo.getPrice(), equalTo(price));
        assertThat(productVariantVo.isInStock(), equalTo(inStock));
        assertThat(productVariantVo.getQuantity(), equalTo(quantity));
        assertThat(productVariantVo.getImageUrl(), equalTo(imageUrl));
        assertThat(productVariantVo.getName(), equalTo(productDto.getName()));
        assertThat(productDto.isAvailable(), equalTo(true));
    }


}
