package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.ecommerce.entitiy.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDto {

    private int id;
    private double purchasePrice;
    private int quantity;
    private LocalDate createdAt;

    private int productVariantId;
    private double total;

    public ExpenseDto(Expense expense) {
        this.id = expense.getId();
        this.purchasePrice = expense.getPurchasePrice();
        this.quantity = expense.getQuantity();
        this.createdAt = expense.getCreatedAt();
        this.productVariantId = expense.getProductVariant().getId();
        this.total = expense.getTotal();
    }
}
