package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.AddressDto;
import com.project.ecommerce.entitiy.Address;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.AddressRepo;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public AddressServiceImpl(AddressRepo addressRepo, UserRepository userRepository, ModelMapper modelMapper) {
        this.addressRepo = addressRepo;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Address> getAddressesByUsername(String username) {
        return addressRepo.findAllByUsername(username);
    }

    @Override
    public Address saveAddressByUsername(AddressDto addressDto, String username) {
        User user = userRepository.getReferenceByUsername(username)
                                .orElseThrow(() -> new UserException("User not found"));

        Address address = new Address(addressDto);
        address.setUser(user);
        return addressRepo.save(address);
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        return addressRepo.findById(id);
    }

    @Override
    public void deleteAddressById(Long id) {
        addressRepo.deleteById(id);
    }
}
