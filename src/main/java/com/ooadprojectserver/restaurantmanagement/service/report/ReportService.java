package com.ooadprojectserver.restaurantmanagement.service.report;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyRevenueRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyRevenue;

import java.util.List;

public interface ReportService {
    List<DailyRevenue> searchDailyRevenue(DailyRevenueRequest request);
}
