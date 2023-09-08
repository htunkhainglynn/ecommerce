package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.User;
import io.lettuce.core.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> getReferenceByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE 'USER' in (SELECT r FROM u.roles r)")
    int findTotalClients();
}
