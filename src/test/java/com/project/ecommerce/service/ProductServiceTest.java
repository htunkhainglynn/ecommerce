package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Organization;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import javax.swing.text.html.Option;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql"})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/product/create.txt")
    void testCreateProduct(
            Integer sku, Double weight, String name, String description, Boolean available,
            String size1, String color1, Double price1, Boolean inStock1, Integer quantity1,
            Long productId1, Double purchasePrice1, String imageUrl1, String size2, String color2,
            Double price2, Boolean inStock2, Integer quantity2, Long productId2, Double purchasePrice2,
            String imageUrl2, String vendor, String category
    ) {

        ProductVariantDto pv1 = ProductVariantDto.builder().size(size1).color(color1)
                .price(price1).inStock(inStock1).quantity(quantity1).purchasePrice(purchasePrice1)
                .imageUrl(imageUrl1).build();

        ProductVariantDto pv2 = ProductVariantDto.builder().size(size2).color(color2)
                .price(price2).inStock(inStock2).quantity(quantity2).purchasePrice(purchasePrice2)
                .imageUrl(imageUrl2).build();

        List<ProductVariantDto> pvList = List.of(pv1, pv2);

        Organization organization = Organization.builder().vendor(vendor).category(category).build();

        ProductDto product = ProductDto.builder().sku(sku).weight(weight).name(name).description(description)
                .available(available).productVariants(pvList).organization(organization).build();

        // check product
        ProductDto result = productService.saveProduct(product);
        assertThat(result.getSku(), equalTo(sku));
        assertThat(result.getWeight(), equalTo(weight));
        assertThat(result.getName(), equalTo(name));
        assertThat(result.getDescription(), equalTo(description));
        assertThat(result.isAvailable(), equalTo(available));
        assertThat(result.getOrganization().getVendor(), equalTo(vendor));
        assertThat(result.getOrganization().getCategory(), equalTo(category));
        assertThat(result.getProductVariants(), hasSize(2));
        assertThat(result.getProductVariants().get(0).getSize(), equalTo(size1));
        assertThat(result.getProductVariants().get(0).getColor(), equalTo(color1));
        assertThat(result.getProductVariants().get(0).getPrice(), equalTo(price1));
        assertThat(result.getProductVariants().get(0).isInStock(), equalTo(inStock1));
        assertThat(result.getProductVariants().get(0).getQuantity(), equalTo(quantity1));
        assertThat(result.getProductVariants().get(0).getProduct_id(), equalTo(productId1));
        assertThat(result.getProductVariants().get(0).getImageUrl(), equalTo(imageUrl1));
        assertThat(result.getProductVariants().get(1).getSize(), equalTo(size2));
        assertThat(result.getProductVariants().get(1).getColor(), equalTo(color2));
        assertThat(result.getProductVariants().get(1).getPrice(), equalTo(price2));
        assertThat(result.getProductVariants().get(1).isInStock(), equalTo(inStock2));
        assertThat(result.getProductVariants().get(1).getQuantity(), equalTo(quantity2));
        assertThat(result.getProductVariants().get(1).getProduct_id(), equalTo(productId2));
        assertThat(result.getProductVariants().get(1).getImageUrl(), equalTo(imageUrl2));
    }

    @Order(2)
    @Test
    void testGetAllProducts() {
        Page<ProductDto> productDtoPage =  productService.getAllProducts(null,
                                        true, Optional.empty(), Optional.empty());

        assertThat(productDtoPage.getTotalElements(), equalTo(2L));
        assertThat(productDtoPage.getTotalPages(), equalTo(1));
        assertThat(productDtoPage.getContent(), hasSize(2));

        productDtoPage =  productService.getAllProducts(null,
                false, Optional.empty(), Optional.empty());

        assertThat(productDtoPage.getTotalElements(), equalTo(1L));
        assertThat(productDtoPage.getTotalPages(), equalTo(1));
        assertThat(productDtoPage.getContent(), hasSize(1));

    }

}
