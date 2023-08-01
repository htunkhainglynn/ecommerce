package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findOneByUsername(String username);
}
