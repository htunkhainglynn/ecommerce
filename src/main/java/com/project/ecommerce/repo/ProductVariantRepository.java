package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    @Query("SELECT pv.imageUrl FROM ProductVariant pv WHERE pv.id = ?1")
    Optional<String> findImageUrlById(Integer id);
}
