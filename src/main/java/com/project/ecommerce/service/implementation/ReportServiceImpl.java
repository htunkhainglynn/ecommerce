package com.project.ecommerce.service.implementation;

import com.project.ecommerce.entitiy.OrderItem;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.entitiy.report.DailyReport;
import com.project.ecommerce.entitiy.report.MonthlyReport;
import com.project.ecommerce.entitiy.report.YearlyReport;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.repo.report.DailyReportRepo;
import com.project.ecommerce.repo.report.MonthlyReportRepo;
import com.project.ecommerce.repo.report.YearlyReportRepo;
import com.project.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;

    private final ProductVariantRepository productVariantRepository;

    private final DailyReportRepo dailyReportRepo;

    private final MonthlyReportRepo monthlyReportRepo;

    private final YearlyReportRepo yearlyReportRepo;

    @Autowired
    public ReportServiceImpl(OrderRepository orderRepository,
                             ProductVariantRepository productVariantRepository,
                             DailyReportRepo dailyReportRepo,
                             MonthlyReportRepo monthlyReportRepo,
                             YearlyReportRepo yearlyReportRepo) {
        this.orderRepository = orderRepository;
        this.productVariantRepository = productVariantRepository;
        this.dailyReportRepo = dailyReportRepo;
        this.monthlyReportRepo = monthlyReportRepo;
        this.yearlyReportRepo = yearlyReportRepo;
    }

    @Override
    public void calculateDailyReport(LocalDate today) {
        double revenue = orderRepository.findDailyRevenue(today);

        List<List<OrderItem>> orders = orderRepository.findOrderItemsByOrderDate(today);

        // calculate daily sales
        double sales = calculateSales(orders);

        dailyReportRepo.save(DailyReport.builder()
                .date(today)
                .revenue(revenue)
                .sales(sales)
                .day(today.getDayOfWeek().toString())
                .build());
    }

    @Override
    public void calculateMonthlyReport(int year, String month) {
        LocalDate endDate = LocalDate.now();
        // 1 month ago + 1 day
        LocalDate startDate = endDate.minusMonths(1).plusDays(1);

        double revenue = orderRepository.findRevenueBetweenDates(startDate, endDate);

        List<List<OrderItem>> orders = orderRepository.findOrderItemsBetweenDates(startDate, endDate);

        // calculate monthly sales
        double sales = calculateSales(orders);

        monthlyReportRepo.save(MonthlyReport.builder()
                .revenue(revenue)
                .sales(sales)
                .year(year)
                .month(month)
                .build());
    }

    @Override
    public void calculateYearlyReport(int year) {
        LocalDate endDate = LocalDate.now();

        // january 1st of the current year
        LocalDate startDate = LocalDate.of(endDate.getYear(), 1, 1);

        double revenue = orderRepository.findRevenueBetweenDates(startDate, endDate);

        List<List<OrderItem>> orders = orderRepository.findOrderItemsBetweenDates(startDate, endDate);

        // calculate yearly sales
        double sales = calculateSales(orders);

        yearlyReportRepo.save(YearlyReport.builder()
                .revenue(revenue)
                .sale(sales)
                .year(year)
                .build());
    }

    private static double calculateSales(List<List<OrderItem>> orders) {
        return orders.stream().map(orderList -> orderList.stream().map(orderItem -> {
            double purchasePrice = orderItem.getProductVariant().getPurchasePrice();
            int quantity = orderItem.getQuantity();
            return purchasePrice * quantity;
        }).reduce(0.0, Double::sum)).reduce(0.0, Double::sum);
    }
}
