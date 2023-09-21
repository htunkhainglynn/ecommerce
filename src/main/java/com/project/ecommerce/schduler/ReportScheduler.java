package com.project.ecommerce.schduler;

import com.project.ecommerce.service.BalanceService;
import com.project.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReportScheduler {

    private final ReportService reportService;

    private final BalanceService balanceService;


    @Autowired
    public ReportScheduler(ReportService reportService,
                           BalanceService balanceService) {
        this.reportService = reportService;
        this.balanceService = balanceService;
    }

    // Schedule daily sales calculation at 11:59:59 PM
    @Scheduled(cron = "59 59 23 * * ?")
    public void generateDailyReport() {

        LocalDate today = LocalDate.now();

        // Calculate and store daily sales
        reportService.calculateDailyReport(today);

        balanceService.calculateDailyBalance(today);

    }

    // Schedule monthly sales calculation at 11:59:59 PM on the last day of the month
    @Scheduled(cron = "59 59 23 L * ?")
    public void generateMonthlyReport() {

        int year = LocalDate.now().getYear();

        String month = LocalDate.now().getMonth().toString();

        // Calculate and store monthly sales
        reportService.calculateMonthlyReport(year, month);

        balanceService.calculateMonthlyService(year, month);
    }

    // Schedule yearly sales calculation at 11:59:59 PM on December 31
    @Scheduled(cron = "59 59 23 31 12 ?")
    public void generateYearlyReport() {

        int year = LocalDate.now().getYear();

        // Calculate and store yearly sales
        reportService.calculateYearlyReport(year);

        balanceService.calculateYearlyBalance(year);
    }
}
