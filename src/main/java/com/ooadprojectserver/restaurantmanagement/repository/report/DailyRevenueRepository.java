package com.ooadprojectserver.restaurantmanagement.repository.report;

import com.ooadprojectserver.restaurantmanagement.model.report.DailyRevenue;
import com.ooadprojectserver.restaurantmanagement.repository.custom.DailyRevenueRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRevenueRepository extends JpaRepository<DailyRevenue, Long>, DailyRevenueRepositoryCustom {
}
