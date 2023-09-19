package com.project.ecommerce.service;

import com.project.ecommerce.dto.ExpenseDto;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    List<ExpenseDto> getExpenseByProductVariantId(Integer id);

    void updateExpenseHistory(Integer id, ExpenseDto expenseDto);

    Optional<ExpenseDto> getExpenseById(int id);
}
