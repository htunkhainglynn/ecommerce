package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddressDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql","/sql/user.sql", "/sql/address.sql"})
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/address/create.txt")
    void testCreateAddressByUsername(String street, String city, String postalCode, String username) {
        AddressDto addressDto = AddressDto.builder()
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .build();

        addressService.saveAddressByUsername(addressDto, username);

        // check result
        
    }
}
