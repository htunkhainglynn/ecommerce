package com.project.ecommerce.repo.report;

import com.project.ecommerce.entitiy.report.YearlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface YearlyReportRepo extends JpaRepository<YearlyReport, Integer> {


}
