package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Organization;
import com.project.ecommerce.vo.ProductVariantVo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql",  "/sql/product.sql"})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired ProductVariantService productVariantService;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/product/create.txt")
    @WithMockUser(authorities = {"ADMIN"})
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
        assertThat(result.getProductVariants().get(0).getProductId(), equalTo(productId1));
        assertThat(result.getProductVariants().get(0).getImageUrl(), equalTo(imageUrl1));
        assertThat(result.getProductVariants().get(1).getSize(), equalTo(size2));
        assertThat(result.getProductVariants().get(1).getColor(), equalTo(color2));
        assertThat(result.getProductVariants().get(1).getPrice(), equalTo(price2));
        assertThat(result.getProductVariants().get(1).isInStock(), equalTo(inStock2));
        assertThat(result.getProductVariants().get(1).getQuantity(), equalTo(quantity2));
        assertThat(result.getProductVariants().get(1).getProductId(), equalTo(productId2));
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

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/product/create-duplication.txt")
    @WithMockUser(authorities = {"ADMIN"})
    void testCreateDuplication(Integer sku, double weight, String name, String description, boolean available) {
        ProductDto productDto = ProductDto.builder()
                .description(description).available(available)
                .sku(sku).weight(weight).name(name).build();

        Organization organization = Organization.builder().vendor("vendor").category("category").build();
        productDto.setOrganization(organization);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productService.saveProduct(productDto);
        });
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/product/findById.txt")
    void testFindById(long id, Integer sku, double weight, String name, String description, boolean available,
                      double averageRating, String image, String price) {
        Optional<ProductDto> productDtoOptional = productService.getProductById(id);
        assertThat(productDtoOptional.isPresent(), equalTo(true));
        ProductDto productDto = productDtoOptional.get();
        assertThat(productDto.getSku(), equalTo(sku));
        assertThat(productDto.getWeight(), equalTo(weight));
        assertThat(productDto.getName(), equalTo(name));
        assertThat(productDto.getDescription(), equalTo(description));
        assertThat(productDto.isAvailable(), equalTo(available));
        assertThat(productDto.getAverageRating(), equalTo(averageRating));
        assertThat(productDto.getImage(), equalTo(image));
        assertThat(productDto.getPrice(), equalTo(Double.parseDouble(price)));
    }

    @Order(5)
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testDeleteProduct() {
        Optional<ProductDto> productDtoOptional = productService.getProductById(1L);
        assertThat(productDtoOptional.isPresent(), equalTo(true));
        List<ProductVariantDto> productVariantDtoList = productDtoOptional.get().getProductVariants();
        assertThat(productVariantDtoList, hasSize(2));
        productService.deleteProduct(1L);
        productDtoOptional = productService.getProductById(1L);
        assertThat(productDtoOptional.isPresent(), equalTo(false));
        productVariantDtoList.forEach(productVariantDto -> {
            Optional<ProductVariantVo> productVariantDtoOptional = productVariantService.getProductVariantById(productVariantDto.getId());
            assertThat(productVariantDtoOptional.isPresent(), equalTo(false));
        });
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/product/update.txt")
    @WithMockUser(authorities = {"ADMIN"})
    void testUpdate(long id, Integer sku,double weight, String name,
                    String description, boolean available) {
        Optional<ProductDto> productDtoOptional = productService.getProductById(id);
        assertThat(productDtoOptional.isPresent(), equalTo(true));
        ProductDto productDto = productDtoOptional.get();
        productDto.setId(id);
        productDto.setSku(sku);
        productDto.setWeight(weight);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setAvailable(available);

        productService.saveProduct(productDto);

        productDtoOptional = productService.getProductById(id);
        assertThat(productDtoOptional.isPresent(), equalTo(true));
        productDto = productDtoOptional.get();
        assertThat(productDto.getSku(), equalTo(sku));
        assertThat(productDto.getWeight(), equalTo(weight));
        assertThat(productDto.getName(), equalTo(name));
        assertThat(productDto.getDescription(), equalTo(description));
        assertThat(productDto.isAvailable(), equalTo(available));
    }
}
