package com.ooadprojectserver.restaurantmanagement.service.report;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.InventoryReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.MonthlyReportRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.InventoryReport;
import com.ooadprojectserver.restaurantmanagement.model.report.MonthlyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.YearlyReport;

import java.util.List;

public interface ReportService {
    List<DailyReport> searchDailyRevenue(DailyReportRequest request);

    List<MonthlyReport> getMonthlyRevenueByYear(MonthlyReportRequest request);

    List<YearlyReport> getYearlyRevenue();

    List<InventoryReport> getInventoryReport(InventoryReportRequest request);
}
