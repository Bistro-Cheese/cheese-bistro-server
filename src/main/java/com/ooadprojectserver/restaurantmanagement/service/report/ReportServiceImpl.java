package com.ooadprojectserver.restaurantmanagement.service.report;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.MonthlyReportRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.MonthlyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.YearlyReport;
import com.ooadprojectserver.restaurantmanagement.repository.report.DailyReportRepository;
import com.ooadprojectserver.restaurantmanagement.repository.report.MonthlyReportRepository;
import com.ooadprojectserver.restaurantmanagement.repository.report.YearlyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final DailyReportRepository dailyReportRepository;
    private final MonthlyReportRepository monthlyReportRepository;
    private final YearlyReportRepository yearlyReportRepository;

    @Override
    public List<DailyReport> searchDailyRevenue(DailyReportRequest request) {
        return dailyReportRepository.search(request);
    }

    @Override
    public List<MonthlyReport> getMonthlyRevenueByYear(MonthlyReportRequest request) {
        return monthlyReportRepository.findByYear(request.getYear());
    }

    @Override
    public List<YearlyReport> getYearlyRevenue() {
        return yearlyReportRepository.findAll();
    }
}
