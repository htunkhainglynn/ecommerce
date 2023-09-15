package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.vo.ProductVariantVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    @Query("SELECT COUNT(pv) FROM ProductVariant pv where pv.quantity > 0")
    int findTotalProducts();

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.createdAt = ?1")
    List<ProductVariant> findByDate(LocalDate today);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.createdAt BETWEEN ?1 AND ?2")
    List<ProductVariant> findBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = ?1")
    Optional<List<ProductVariant>> findByProductId(Integer id);

    @Query("SELECT p.id FROM ProductVariant pv JOIN pv.product p WHERE pv.id = ?1")
    Long findProductIdById(Integer id);
}
