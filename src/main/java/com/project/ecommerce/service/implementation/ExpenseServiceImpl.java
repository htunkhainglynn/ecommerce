package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.repo.BalanceRepo;
import com.project.ecommerce.repo.ExpenseRepo;
import com.project.ecommerce.service.ExpenseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;

    private final BalanceRepo balanceRepo;

    public ExpenseServiceImpl(ExpenseRepo expenseRepo, BalanceRepo balanceRepo) {
        this.expenseRepo = expenseRepo;
        this.balanceRepo = balanceRepo;
    }


    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ExpenseDto> getExpenseByProductVariantId(Integer id) {
        List<Expense> expenses = expenseRepo.getExpenseByProductVariantId(id);
        return expenses.stream().map(ExpenseDto::new).toList();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateExpenseHistory(Integer id, ExpenseDto expenseDto) {
        double purchasePrice = expenseDto.getPurchasePrice();
        int quantity = expenseDto.getQuantity();
        double oldTotal = expenseRepo.getTotalById(id);
        double newTotal = purchasePrice * quantity;
        double difference = newTotal - oldTotal;
        expenseRepo.updateExpenseHistory(id, purchasePrice, quantity, newTotal);

        // update propagated changes in balance table
        // day propagation
        if (!expenseDto.getCreatedAt().equals(LocalDate.now())) {
            balanceRepo.updateExpenses(expenseDto.getCreatedAt(), difference);
        }

        // month propagation
        if (!expenseDto.getCreatedAt().getMonth().equals(LocalDate.now().getMonth())) {
            LocalDate endOfMonth = expenseDto.getCreatedAt().withDayOfMonth(expenseDto.getCreatedAt().lengthOfMonth());
            balanceRepo.updateExpenses(endOfMonth, difference);
        }

        // year propagation
        if (expenseDto.getCreatedAt().getYear() != LocalDate.now().getYear()) {
            LocalDate endOfYear = expenseDto.getCreatedAt().withDayOfYear(expenseDto.getCreatedAt().lengthOfYear());
            balanceRepo.updateExpenses(endOfYear, difference);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<ExpenseDto> getExpenseById(int id) {
        return expenseRepo.findById((long) id).map(ExpenseDto::new);
    }
}
