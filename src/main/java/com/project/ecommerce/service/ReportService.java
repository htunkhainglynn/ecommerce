package com.project.ecommerce.service;

import java.time.LocalDate;

public interface ReportService {
    void calculateDailyReport(LocalDate today);

    void calculateMonthlyReport(int year, String month);

    void calculateYearlyReport(int year);

}
