package com.project.ecommerce.service;

import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;

import java.util.List;
import java.util.Optional;

public interface DashboardService {
    DashBoardSummaryVo getSummary();

    List<GraphDataVo> getDailyGraphData();

    Optional<GraphDataVo> getMonthlyGraphData(Integer year);

    List<GraphDataVo> getYearlyGraphData();
}
