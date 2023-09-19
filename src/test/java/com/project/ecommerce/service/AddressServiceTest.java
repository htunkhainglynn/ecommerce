package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql","/sql/user.sql", "/sql/address.sql"})
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/address/create.txt")
    @WithMockUser(authorities = {"USER"})
    void testCreateAddressByUsername(String street, String city, String postalCode, String username, int size) {
        AddressDto addressDto = AddressDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();

        addressService.saveAddressByUsername(addressDto, username);

        // check result
        assertThat(addressService.getAddressesByUsername(username), hasSize(size));
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/address/create-null.txt")
    @WithMockUser(authorities = {"USER"})
    void testCreateWithNull(String city, String postalCode, String street, String username) {
        AddressDto addressDto = AddressDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> addressService.saveAddressByUsername(addressDto, username));
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/address/update.txt")
    @WithMockUser(authorities = {"USER"})
    void testUpdate(long id, String city, String postalCode, String street, String username) {
        Optional<Address> address = addressService.getAddressById(id);
        assertTrue(address.isPresent());
        AddressDto addressDto = AddressDto.builder()
                .id(id)
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();
        addressService.saveAddressByUsername(addressDto, username);

        // check result
        address = addressService.getAddressById(id);
        assertTrue(address.isPresent());
        assertEquals(address.get().getCity(), city);
        assertEquals(address.get().getPostalCode(), postalCode);
        assertEquals(address.get().getStreet(), street);
        assertEquals(address.get().getUser().getUsername(),username);
    }

    @Order(4)
    @Test
    void testGetAddressById() {
        Optional<Address> address = addressService.getAddressById(1L);
        assertTrue(address.isPresent());
        assertEquals(address.get().getCity(), "Yangon");
        assertEquals(address.get().getPostalCode(), "10001");
        assertEquals(address.get().getStreet(), "123 Main St");
        assertEquals(address.get().getUser().getUsername(),"user1");

        address = addressService.getAddressById(100L);
        assertTrue(address.isEmpty());
    }

    @Order(5)
    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteAddressById() {
        Optional<Address> address = addressService.getAddressById(1L);
        assertTrue(address.isPresent());
        addressService.deleteAddressById(1L);
        address = addressService.getAddressById(1L);
        assertTrue(address.isEmpty());
    }
}
