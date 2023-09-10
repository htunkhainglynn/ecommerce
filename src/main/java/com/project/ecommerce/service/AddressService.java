package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> getAddressesByUsername(String username);

    Address saveAddressByUsername(AddressDto addressDto, String username);

    Optional<Address> getAddressById(Long id);

    void deleteAddressById(Long id);
}
