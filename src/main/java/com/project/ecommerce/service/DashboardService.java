package com.project.ecommerce.service;

import com.project.ecommerce.entitiy.balance.Balance;
import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DashboardService {
    DashBoardSummaryVo getSummary();

    List<GraphDataVo> getDailyGraphData();

    Optional<GraphDataVo> getMonthlyGraphData(Integer year);

    List<GraphDataVo> getYearlyGraphData();

    List<Balance> getBalance(LocalDate startDate, LocalDate endDate);

    List<Balance> getMonthlyBalance(Integer year);

    List<Balance> getYearlyBalance(int startYear, int endYear);
}
