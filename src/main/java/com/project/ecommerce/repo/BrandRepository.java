package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
