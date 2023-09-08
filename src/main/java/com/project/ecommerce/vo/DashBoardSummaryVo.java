package com.project.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardSummaryVo {

    private int totalClients;

    private double totalSales;

    private int totalOrders;

    private int totalProducts;
}
