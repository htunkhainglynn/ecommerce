package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.service.AddressService;
import com.project.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @Autowired
    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    // get addresses by username
    @GetMapping
    public ResponseEntity<List<Address>> getAddressesByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(addressService.getAddressesByUsername(username));
    }

    // create address by username
    @PostMapping
    public ResponseEntity<Address> createAddressByUsername(AddressDto addressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(addressService.saveAddressByUsername(addressDto, username));
    }

    // update address by id
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddressById(@PathVariable Long id, AddressDto addressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressDto.setId(id);
            return ResponseEntity.ok(addressService.saveAddressByUsername(addressDto, username));
        }
        return ResponseEntity.notFound().build();
    }

    // delete address by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressService.deleteAddressById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
