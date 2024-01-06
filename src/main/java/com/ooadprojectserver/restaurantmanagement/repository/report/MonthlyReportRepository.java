package com.ooadprojectserver.restaurantmanagement.repository.report;

import com.ooadprojectserver.restaurantmanagement.model.report.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long>{
    List<MonthlyReport> findByYear(Integer year);
}
