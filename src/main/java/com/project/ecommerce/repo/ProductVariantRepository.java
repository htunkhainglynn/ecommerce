package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
}
