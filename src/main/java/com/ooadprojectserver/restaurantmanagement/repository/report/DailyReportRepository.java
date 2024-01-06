package com.ooadprojectserver.restaurantmanagement.repository.report;

import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;
import com.ooadprojectserver.restaurantmanagement.repository.custom.DailyRevenueRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long>, DailyRevenueRepositoryCustom {
}
