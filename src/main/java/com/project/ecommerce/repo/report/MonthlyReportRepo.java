package com.project.ecommerce.repo.report;

import com.project.ecommerce.entitiy.report.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyReportRepo extends JpaRepository<MonthlyReport, Long> {

    Optional<List<MonthlyReport>> findByYear(Integer year);
}
