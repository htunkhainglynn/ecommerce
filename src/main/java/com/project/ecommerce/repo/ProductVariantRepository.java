package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    @Query("SELECT pv.imageUrl FROM ProductVariant pv WHERE pv.id = ?1")
    Optional<String> findImageUrlById(Integer id);

    @Query("SELECT pv.quantity FROM ProductVariant pv WHERE pv.id = ?1")
    Optional<Integer> findQuantityById(Integer key);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.quantity = ?2 WHERE pv.id = ?1")
    void updateQuantityById(Integer key, Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.inStock = ?2 WHERE pv.id = ?1")
    void updateInStockById(Integer key, boolean b);
}
