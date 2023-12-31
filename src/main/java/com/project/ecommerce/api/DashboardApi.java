package com.project.ecommerce.api;

import com.project.ecommerce.entitiy.balance.Balance;
import com.project.ecommerce.service.DashboardService;
import com.project.ecommerce.service.ReportService;
import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardApi {

    private final DashboardService dashboardService;
    private final ReportService reportService;

    @Autowired
    public DashboardApi(DashboardService dashboardService, ReportService reportService) {
        this.dashboardService = dashboardService;
        this.reportService = reportService;
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                          @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        List<Balance> balance = dashboardService.getBalance(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("title", "Balance Report from %s to %s".formatted(startDate, endDate));
        result.put("income", balance.stream().map(Balance::getIncome).toList());
        result.put("expenses", balance.stream().map(Balance::getExpenses).toList());
        result.put("profit", balance.stream().map(Balance::getProfit).toList());
        result.put("date", balance.stream().map(Balance::getDate).toList());
        return ok(result);
    }

    @GetMapping("/balance/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyBalance(@RequestParam(value = "year", required = false) Integer year) {
        List<Balance> balance = dashboardService.getMonthlyBalance(year);
        Map<String, Object> result = new HashMap<>();
        result.put("title", "%d Monthly Balance Report".formatted(year));
        result.put("income", balance.stream().map(Balance::getIncome).toList());
        result.put("expenses", balance.stream().map(Balance::getExpenses).toList());
        result.put("profit", balance.stream().map(Balance::getProfit).toList());
        result.put("month", balance.stream().map(Balance::getMonth).toList());
        return ok(result);
    }

    @GetMapping("/balance/yearly")
    public ResponseEntity<Map<String, Object>> getYearlyBalance(@RequestParam(required = false) int startYear, @RequestParam(required = false) int endYear) {
        List<Balance> balance = dashboardService.getYearlyBalance(startYear, endYear);
        Map<String, Object> result = new HashMap<>();
        result.put("title", "Yearly Balance Report");
        result.put("income", balance.stream().map(Balance::getIncome).toList());
        result.put("expenses", balance.stream().map(Balance::getExpenses).toList());
        result.put("profit", balance.stream().map(Balance::getProfit).toList());
        result.put("year", balance.stream().map(Balance::getYear).toList());
        return ok(result);
    }

}
