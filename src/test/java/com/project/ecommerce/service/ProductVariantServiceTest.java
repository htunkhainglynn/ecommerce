package com.project.ecommerce.service;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.vo.ProductVariantVo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql"})
public class ProductVariantServiceTest {

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BalanceService balanceService;

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

    @Order(2)
    @ParameterizedTest
    @WithMockUser(authorities = {"ADMIN"})
    @CsvFileSource(resources = {"/csv/productVariant/getProductVariantById.txt"})
    void testGetProductVariantById(LocalDate created_at, boolean in_stock, double price,
                                   int quantity, LocalDate updated_at, long product_id, String color,
                                   String image_url, String size, int productVariantId) {
        Optional<ProductVariantVo> productVariantVo = productVariantService.getProductVariantById(productVariantId);

        assertTrue(productVariantVo.isPresent());
        assertThat(created_at, equalTo(productVariantVo.get().getCreatedAt()));
        assertThat(in_stock, is(productVariantVo.get().isInStock()));
        assertThat(price, equalTo(productVariantVo.get().getPrice()));
        assertThat(quantity, equalTo(productVariantVo.get().getQuantity()));
        assertThat(updated_at, equalTo(productVariantVo.get().getUpdatedAt()));
        assertThat(product_id, equalTo(productVariantVo.get().getProductId()));
        assertThat(color, equalTo(productVariantVo.get().getColor()));
        assertThat(image_url, equalTo(productVariantVo.get().getImageUrl()));
        assertThat(size, equalTo(productVariantVo.get().getSize()));
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(authorities = {"ADMIN"})
    @CsvFileSource(resources = {"/csv/productVariant/update.txt"})
    void testUpdateProductVariant(LocalDate created_at, boolean in_stock, double price,
                                  double purchasePrice, int quantity,
                                  LocalDate updated_at, long product_id, String color,
                                  String image_url, String size, int productVariantId) {
        ProductVariantDto productVariantDto = ProductVariantDto.builder()
                                                        .id(productVariantId)
                                                        .size(size)
                                                        .color(color)
                                                        .price(price)
                                                        .purchasePrice(purchasePrice)
                                                        .inStock(in_stock)
                                                        .quantity(quantity)
                                                        .product_id(product_id)
                                                        .imageUrl(image_url)
                                                        .purchasePrice(purchasePrice)
                                                        .build();

        productVariantService.saveProductVariant(productVariantDto);

        Optional<ProductVariantVo> productVariantVo = productVariantService.getProductVariantById(productVariantId);

        // check asserts
        assertTrue(productVariantVo.isPresent());
        assertThat(created_at, equalTo(productVariantVo.get().getCreatedAt()));
        assertThat(in_stock, is(productVariantVo.get().isInStock()));
        assertThat(price, equalTo(productVariantVo.get().getPrice()));
        assertThat(quantity, equalTo(productVariantVo.get().getQuantity()));
        assertThat(updated_at, equalTo(productVariantVo.get().getUpdatedAt()));
        assertThat(product_id, equalTo(productVariantVo.get().getProductId()));
        assertThat(color, equalTo(productVariantVo.get().getColor()));
        assertThat(image_url, equalTo(productVariantVo.get().getImageUrl()));
        assertThat(size, equalTo(productVariantVo.get().getSize()));
    }

    @Order(4)
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testDeleteProductVariantById() {

        long productId = productVariantService.getProductIdByPvId(1);

        int productVariantSize = productService.getProductById(productId).orElseThrow().getProductVariants().size();

        productVariantService.deleteProductVariant(1);

        int newProductVariantSize = productService.getProductById(productId).orElseThrow().getProductVariants().size();

        Optional<ProductVariantVo> productVariantVo = productVariantService.getProductVariantById(1);

        // asserts
        assertTrue(productVariantVo.isEmpty());
        assertThat(productVariantSize, equalTo(newProductVariantSize + 1));
    }

    @Order(5)
    @ParameterizedTest
    @WithMockUser(authorities = {"ADMIN"})
    @CsvFileSource(resources = {"/csv/productVariant/updateExpenseHistory.txt"})
    void testUpdateExpense(int id, double purchasePrice, int quantity, LocalDate createdAt) {
        ExpenseDto expenseDto = ExpenseDto.builder()
                                    .purchasePrice(purchasePrice)
                                    .quantity(quantity)
                                    .createdAt(createdAt)
                                    .build();
        expenseService.updateExpenseHistory(id, expenseDto);

        Optional<ExpenseDto> expenseDtoOptional = expenseService.getExpenseById(id);

        // asserts
        assertTrue(expenseDtoOptional.isPresent());
        assertThat(expenseDtoOptional.get().getPurchasePrice(), equalTo(purchasePrice));
        assertThat(expenseDtoOptional.get().getQuantity(), equalTo(quantity));
        assertThat(expenseDtoOptional.get().getCreatedAt(), equalTo(createdAt));

        // check propagated changes
        assertThat(balanceService.getDailyBalance(createdAt).getExpenses(), equalTo(purchasePrice * quantity));
    }
}
