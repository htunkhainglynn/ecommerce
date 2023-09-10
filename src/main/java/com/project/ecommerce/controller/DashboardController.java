package com.project.ecommerce.controller;

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
public class DashboardController {

    private final DashboardService dashboardService;
    private final ReportService reportService;

    @Autowired
    public DashboardController(DashboardService dashboardService, ReportService reportService) {
        this.dashboardService = dashboardService;
        this.reportService = reportService;
    }

    // get dashboard summary
    @GetMapping("/summary")
    public ResponseEntity<DashBoardSummaryVo> getSummary() {
        return ok(dashboardService.getSummary());
    }

    @GetMapping("/graph/daily")
    public ResponseEntity<Map<String, Object>> getDailyGraphData() {
        Map<String, Object> result = new HashMap<>();
        result.put("title", "hello, world!");
        result.put("revenue", List.of(1, 2, 3, 4, 5));
        result.put("sales", List.of(5, 4, 3, 2, 1));
        return ok(result);
    }

    @GetMapping("/graph/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyGraphData(@RequestParam(value = "year", required = false) Integer year) {
        Optional<GraphDataVo> graphData = dashboardService.getMonthlyGraphData(year);

        if (graphData.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("title", "%d Monthly Report".formatted(year));
            result.put("revenue", graphData.get().getRevenue());
            result.put("sales", graphData.get().getSales());
            return ok(result);
        }

        return ok(null);
    }

    @GetMapping("/graph/yearly")
    public ResponseEntity<List<GraphDataVo>> getYearlyGraphData() {
        return ok(dashboardService.getYearlyGraphData());
    }

    @GetMapping("/test")
    public void getTest() {
        LocalDate today = LocalDate.now();
        reportService.calculateDailyReport(today);
    }
}
