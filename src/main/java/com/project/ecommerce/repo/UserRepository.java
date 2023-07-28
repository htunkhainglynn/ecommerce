package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findOneByUsername(String username);
}
