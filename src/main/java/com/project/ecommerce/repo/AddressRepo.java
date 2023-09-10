package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.username = ?1")
    List<Address> findAllByUsername(String username);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.user.username = ?2")
    Optional<Address> findByIdAndUsername(Integer addressId, String username);
}
