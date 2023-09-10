package com.project.ecommerce.service;

import java.time.LocalDate;
import java.util.Date;

public interface ReportService {
    void calculateDailyReport(LocalDate today);

    void calculateMonthlyReport(int year, String month);

    void calculateYearlyReport(int year);

}
