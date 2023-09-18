package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.balance.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface BalanceRepo extends JpaRepository<Balance, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Balance b SET b.expenses = b.expenses + ?2 WHERE b.date = ?1")
    void updateExpenses(LocalDate now, double difference);
}
