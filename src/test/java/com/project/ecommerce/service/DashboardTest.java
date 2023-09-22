package com.project.ecommerce.service;

import com.project.ecommerce.entitiy.balance.Balance;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = {"/sql/init-database.sql", "/sql/user.sql", "/sql/address.sql", "/sql/balance.sql"})
public class DashboardTest {

    @Autowired
    private DashboardService dashboardService;

    @Test
    @Order(1)
    void testGetBalance() {
//        List<Balance> balance = dashboardService.getBalance(null, null);
//
//        // assert
//        assertThat(balance, hasSize(7));
//        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(27005.0));

        List<Balance> balance = dashboardService.getBalance(LocalDate.of(2023, 9, 13), null);

        // assert
        assertThat(balance, hasSize(9));
        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(35006.0));

        balance = dashboardService.getBalance(null, LocalDate.of(2023, 9, 20));

        // assert
        assertThat(balance, hasSize(7));
        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(27004.0));
    }

    @Test
    @Order(2)
    void testMonthlyBalance() {
//        List<Balance> balance = dashboardService.getMonthlyBalance(null);
//
//        // assert
//        assertThat(balance, hasSize(3));
//        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(5000000.0 * 3));

        List<Balance> balance = dashboardService.getMonthlyBalance(2024);

        // assert
        assertThat(balance, hasSize(2));
        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(5000000.0 * 2));
    }

    @Test
    @Order(3)
    void testYearlyBalance() {
//        List<Balance> balance = dashboardService.getYearlyBalance(0, 0);
//
//        // assert
//        assertThat(balance, hasSize(1));
//        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(500000000.0));

        List<Balance> balance = dashboardService.getYearlyBalance(2024, 2025);

        // assert
        assertThat(balance, hasSize(2));
        assertThat(balance.stream().map(Balance::getProfit).reduce(0.0, Double::sum), is(500000000.0 * 2));
    }
 }
