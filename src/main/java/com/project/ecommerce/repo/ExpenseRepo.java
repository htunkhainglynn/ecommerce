package com.project.ecommerce.repo;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.entitiy.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense, Long> {

    @Query("SELECT SUM(e.total) FROM Expense e WHERE e.createdAt = ?1")
    Double findDailyExpenses(LocalDate today);

    @Query("SELECT SUM(e.total) FROM Expense e WHERE e.createdAt BETWEEN ?1 AND ?2")
    Double findExpensesBetweenDates(LocalDate startDate, LocalDate today);

    @Modifying
    @Transactional
    @Query("UPDATE Expense e SET e.purchasePrice = ?2, e.quantity = ?3, e.total = ?4 WHERE e.id = ?1")
    void updateExpenseHistory(Integer id, double purchasePrice, int quantity, double total);

    @Query("SELECT e.total FROM Expense e WHERE e.id = ?1")
    Double getTotalById(Integer id);

    @Query("SELECT e FROM Expense e WHERE e.productVariant.id = ?1")
    List<Expense> getExpenseByProductVariantId(Integer id);
}
