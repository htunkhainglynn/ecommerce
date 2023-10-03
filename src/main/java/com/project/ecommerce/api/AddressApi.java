package com.project.ecommerce.api;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressApi {

    private final AddressService addressService;

    @Autowired
    public AddressApi(AddressService addressService) {
        this.addressService = addressService;
    }

    // get addresses by username
    @GetMapping
    public ResponseEntity<List<Address>> getAddressesByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(addressService.getAddressesByUsername(username));
    }

    // create address by username
    @PostMapping
    public ResponseEntity<Long> createAddressByUsername(@RequestBody AddressDto addressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Address address = addressService.saveAddressByUsername(addressDto, username);
        return ResponseEntity.ok(address.getId());
    }

    // update address by id
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAddressById(@PathVariable Long id, AddressDto addressDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressDto.setId(id);
            Address result = addressService.saveAddressByUsername(addressDto, username);
            return ResponseEntity.ok(result.getId());
        }
        return ResponseEntity.notFound().build();
    }

    // delete address by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressService.deleteAddressById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
