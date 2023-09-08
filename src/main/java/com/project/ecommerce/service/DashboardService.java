package com.project.ecommerce.service;

import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;

import java.util.List;

public interface DashboardService {
    DashBoardSummaryVo getSummary();

    List<GraphDataVo> getGraphData();
}
