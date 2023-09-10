package com.project.ecommerce.service;

import java.time.LocalDate;

public interface BalanceService {
    void calculateDailyBalance(LocalDate today);

    void calculateMonthlyService(int year, String month);

    void calculateYearlyBalance(int year);
}
