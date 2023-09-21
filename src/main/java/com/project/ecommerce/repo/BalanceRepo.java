package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.balance.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface BalanceRepo extends JpaRepository<Balance, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Balance b SET b.expenses = b.expenses + ?2 WHERE b.date = ?1")
    void updateExpenses(LocalDate now, double difference);

    @Query("SELECT b FROM Balance b WHERE b.date = ?1")
    Balance getDailyBalance(LocalDate date);

    @Query("SELECT b FROM Balance b WHERE b.date BETWEEN ?1 AND ?2 AND b.type = 'DAY'")
    List<Balance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Balance b WHERE b.year = ?1 AND b.type = 'MONTH'")
    List<Balance> findByYear(Integer year);

    @Query("SELECT b FROM Balance b WHERE b.year BETWEEN ?1 AND ?2 AND b.type = 'YEAR'")
    List<Balance> findByYearBetween(int startYear, int endYear);
}
