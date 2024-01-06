package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyReportRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;

import java.util.List;

public interface DailyRevenueRepositoryCustom {
    List<DailyReport> search(DailyReportRequest request);
}
