package com.project.ecommerce.service.implementation;

import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.entitiy.balance.Balance;
import com.project.ecommerce.entitiy.balance.Type;
import com.project.ecommerce.repo.BalanceRepo;
import com.project.ecommerce.repo.ExpenseRepo;
import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.BalanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepo balanceRepo;

    private final OrderRepository orderRepository;

    private final ExpenseRepo expenseRepo;

    public BalanceServiceImpl(BalanceRepo balanceRepo,
                              OrderRepository orderRepository,
                              ExpenseRepo expenseRepo) {
        this.balanceRepo = balanceRepo;
        this.orderRepository = orderRepository;
        this.expenseRepo = expenseRepo;
    }

    @Override
    public void calculateDailyBalance(LocalDate today) {
        double income = orderRepository.findDailyRevenue(today);

        double expenses = expenseRepo.findDailyExpenses(today);

        // calculate daily profit
        double profit = income - expenses;

        // save daily balance
        balanceRepo.save(
                Balance.builder()
                        .day(today.getDayOfWeek().toString())
                        .date(today)
                        .income(income)
                        .expenses(expenses)
                        .profit(profit)
                        .type(Type.DAY)
                        .build()
        );
    }

    @Override
    public void calculateMonthlyService(int year, String month) {
        LocalDate today = LocalDate.now();
        // 1 month ago + 1 day
        LocalDate startDate = today.minusMonths(1).plusDays(1);

        double income = orderRepository.findRevenueBetweenDates(startDate, today);


        // calculate monthly expenses
        double expenses = expenseRepo.findExpensesBetweenDates(startDate, today);

        // calculate monthly profit
        double profit = income - expenses;

        // save monthly balance
        balanceRepo.save(
                Balance.builder()
                        .month(month)
                        .year(year)
                        .date(today)
                        .income(income)
                        .expenses(expenses)
                        .profit(profit)
                        .type(Type.MONTH)
                        .build()
        );
    }

    @Override
    public void calculateYearlyBalance(int year) {
        LocalDate today = LocalDate.now();

        // january 1st of the current year
        LocalDate startDate = LocalDate.of(today.getYear(), 1, 1);

        double income = orderRepository.findRevenueBetweenDates(startDate, today);

        // calculate yearly expenses
        double expenses = expenseRepo.findExpensesBetweenDates(startDate, today);

        // calculate yearly profit
        double profit = income - expenses;

        // save yearly balance
        balanceRepo.save(
                Balance.builder()
                        .year(year)
                        .date(today)
                        .income(income)
                        .expenses(expenses)
                        .profit(profit)
                        .type(Type.YEAR)
                        .build()
        );
    }
}
