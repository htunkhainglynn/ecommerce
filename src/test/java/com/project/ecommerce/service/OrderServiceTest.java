package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.dto.OrderDetailDto;
import com.project.ecommerce.dto.OrderItemDto;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.Status;
import com.project.ecommerce.vo.OrderDetailVo;
import com.project.ecommerce.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/product.sql", "/sql/user.sql", "/sql/address.sql", "/sql/orders.sql"})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    void setAuthentication(String username, Role role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(role::name));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Order(1)
    @ParameterizedTest
    @PreAuthorize("hasAuthority('USER')")
    @CsvFileSource(resources = "/csv/order/create.txt")
    void testCreateOrder(Integer productVariantId, Integer quantity, Double price, Double total,
                         Long addressId, String street, String city, Integer postalCode,
                         String street1, String city1, Integer postalCode1){

        setAuthentication("user1", Role.USER);

        OrderItemDto orderItemDto = OrderItemDto.builder()
                .productVariantId(productVariantId)
                .quantity(quantity)
                .price(price)
                .build();

        OrderItemDto orderItemDto2 = OrderItemDto.builder()
                .productVariantId(2)
                .quantity(30)
                .price(39.99)
                .build();

        AddressDto addressDto = AddressDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();

        OrderDetailDto orderDetailDto = OrderDetailDto.builder()
                .orderItems(List.of(orderItemDto, orderItemDto2))
                .addressId(addressId)
                .address(addressDto)
                .totalPrice(total)
                .build();

        OrderDetailVo orderDetailVo = orderService.saveOrder(orderDetailDto);

        Optional<List<OrderItemDto>> orderItems = orderService.getOrderItemsByOrderId(orderDetailVo.getOrderId());

        // assert
        assertThat(orderDetailVo, is(notNullValue()));
        assertThat(orderItems.isPresent(), is(true));
        assertThat(orderDetailVo.getStatus().name(), is("PENDING"));
        assertThat(orderDetailVo.getTotalPrice(), is(total));

        if (addressId != null) {
            Optional<Address> addressTest = addressService.getAddressById(addressId);
            assertThat(addressTest.isPresent(), is(true));
            assertThat(addressTest.get().getStreet(), is(street1));
            assertThat(addressTest.get().getCity(), is(city1));
            assertThat(addressTest.get().getPostalCode(), is(postalCode1));
        }

        if (street != null) {
            assertThat(orderDetailVo.getOrderDate(), is(LocalDate.now()));
            assertThat(orderDetailVo.getAddress().getStreet(), is(street));
            assertThat(orderDetailVo.getAddress().getCity(), is(city));
            assertThat(orderDetailVo.getAddress().getPostalCode(), is(postalCode));
            assertThat(orderItems.get().size(), is(2));
        }
    }

    @Order(2)
    @ParameterizedTest
    @PreAuthorize("hasAuthority('ADMIN')")
    @CsvFileSource(resources = "/csv/order/findById.txt")
    void testFindById(long id, LocalDate createdAt, double totalPrice, String status, int itemCount,
                      String username, String city, int postalCode, String street) {

        setAuthentication("admin", Role.ADMIN);

        Optional<OrderDetailVo> orderDetailVo = orderService.getOrderById(id);

        Optional<List<OrderItemDto>> orderItems = orderService.getOrderItemsByOrderId(id);

        // assert
        assertThat(orderDetailVo.isPresent(), is(true));
        assertThat(orderItems.isPresent(), is(true));
        assertThat(orderDetailVo.get().getOrderId(), is(id));
        assertThat(orderDetailVo.get().getOrderDate(), is(createdAt));
        assertThat(orderDetailVo.get().getTotalPrice(), is(totalPrice));
        assertThat(orderDetailVo.get().getStatus().name(), is(status));
        assertThat(orderDetailVo.get().getUser().getUsername(), is(username));
        assertThat(orderDetailVo.get().getAddress().getStreet(), is(street));
        assertThat(orderDetailVo.get().getAddress().getCity(), is(city));
        assertThat(orderDetailVo.get().getAddress().getPostalCode(), is(postalCode));
        assertThat(orderItems.get().size(), is(itemCount));
    }

    @Order(3)
    @Test
    void testUpdateStatusShipped() {
        setAuthentication("admin", Role.ADMIN);

        orderService.updateStatus(1L, Status.SHIPPED);
        Optional<OrderDetailVo> orderDetailVo = orderService.getOrderById(1L);

        assertThat(orderDetailVo.isPresent(), is(true));
        assertThat(orderDetailVo.get().getStatus().name(), is("SHIPPED"));
    }

    @Order(4)
    @Test
    @PreAuthorize("hasAuthority('USER')")
    void testUpdateStatusReceivedUser() {

        setAuthentication("user1", Role.USER);

        orderService.updateStatus(1L, Status.RECEIVED);
        Optional<OrderDetailVo> orderDetailVo = orderService.getOrderById(1L);

        assertThat(orderDetailVo.isPresent(), is(true));
        assertThat(orderDetailVo.get().getStatus().name(), is("RECEIVED"));
    }

    @Order(5)
    @Test
    void testGetAllOrderByUsername() {

        setAuthentication("user1", Role.USER);

        Page<OrderVo> orderVo = orderService.getAllOrdersByUsername("user1", Optional.empty(), Optional.empty());

        // assert
        assertThat(orderVo.getContent().size(), is(2));
        assertThat(orderVo.getTotalElements(), is(2L));
        assertThat(orderVo.getTotalPages(), is(1));
        assertThat(orderVo.getContent().get(0).getOrderDate(), is("2023-09-13"));
        assertThat(orderVo.getContent().get(0).getStatus().name(), is("PENDING"));
        assertThat(orderVo.getContent().get(0).getTotalPrice(), is(99.99));
    }

}
