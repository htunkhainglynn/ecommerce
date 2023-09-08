package com.project.ecommerce.controller;

import com.project.ecommerce.service.DashboardService;
import com.project.ecommerce.service.ReportService;
import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/graph")
    public ResponseEntity<List<GraphDataVo>> getGraphData() {
        return ok(dashboardService.getGraphData());
    }

    @GetMapping("/test")
    public void getTest() {
        LocalDate today = LocalDate.now();
        reportService.calculateDailyReport(today);
    }
}
