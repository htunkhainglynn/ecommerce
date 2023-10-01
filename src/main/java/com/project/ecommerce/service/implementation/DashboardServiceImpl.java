package com.project.ecommerce.service.implementation;

import com.project.ecommerce.entitiy.balance.Balance;
import com.project.ecommerce.entitiy.report.MonthlyReport;
import com.project.ecommerce.repo.BalanceRepo;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.repo.report.MonthlyReportRepo;
import com.project.ecommerce.service.DashboardService;
import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    private final ProductVariantRepository productVariantRepository;

    private final OrderRepository orderRepository;

    private final MonthlyReportRepo monthlyReportRepo;

    private final BalanceRepo balanceRepo;

    @Autowired
    public DashboardServiceImpl(UserRepository userRepository,
                                ProductVariantRepository productVariantRepository,
                                OrderRepository orderRepository,
                                BalanceRepo balanceRepo,
                                MonthlyReportRepo monthlyReportRepo) {
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
        this.balanceRepo = balanceRepo;
        this.monthlyReportRepo = monthlyReportRepo;
    }

    @Override
    public DashBoardSummaryVo getSummary() {

        int totalClients = userRepository.findTotalClients();

        int totalProducts = productVariantRepository.findTotalProducts();

        int totalOrders = orderRepository.findTotalOrders();

        double totalSales = orderRepository.findTotalSales();

        return DashBoardSummaryVo.builder()
                .totalClients(totalClients)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalSales(totalSales)
                .build();
    }

    @Override
    public List<GraphDataVo> getDailyGraphData() {
        return null;
    }

    @Override
    public Optional<GraphDataVo> getMonthlyGraphData(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Optional<List<MonthlyReport>> report = monthlyReportRepo.findByYear(year);
        return report.map(monthlyReports -> GraphDataVo.builder()
                .revenue(monthlyReports.stream().map(MonthlyReport::getRevenue).toList())
                .sales(monthlyReports.stream().map(MonthlyReport::getSales).toList())
                .build());
    }

    @Override
    public List<GraphDataVo> getYearlyGraphData() {
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Balance> getBalance(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(7).plusDays(1);
        }

        if (startDate == null) {
            startDate = endDate.minusDays(7).plusDays(1);
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // calculate weekly balance
        return balanceRepo.findByDateBetween(startDate, endDate);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Balance> getMonthlyBalance(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        return balanceRepo.findByYear(year);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Balance> getYearlyBalance(int startYear, int endYear) {
        if (startYear == 0 && endYear == 0) {
            endYear = LocalDate.now().getYear();
            startYear = LocalDate.now().getYear();
        }

        return balanceRepo.findByYearBetween(startYear, endYear);
    }

}
