package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.AddressRepo;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.AddressService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    private final UserRepository userRepository;


    public AddressServiceImpl(AddressRepo addressRepo, UserRepository userRepository) {
        this.addressRepo = addressRepo;
        this.userRepository = userRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Address> getAddressesByUsername(String username) {
        return addressRepo.findAllByUsername(username);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public Address saveAddressByUsername(AddressDto addressDto, String username) {
        User user = userRepository.getReferenceByUsername(username)
                                .orElseThrow(() -> new UserException("User not found"));

        Address address = new Address(addressDto);

        // update case
        if (addressDto.getId() != null) {
            address.setId(addressDto.getId());
        }
        address.setUser(user);
        return addressRepo.save(address);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public Optional<Address> getAddressById(Long id) {
        return addressRepo.findById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public void deleteAddressById(Long id) {
        addressRepo.deleteById(id);
    }
}
