package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
