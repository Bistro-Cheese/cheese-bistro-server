package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.InventoryReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.MonthlyReportRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.InventoryReport;
import com.ooadprojectserver.restaurantmanagement.model.report.MonthlyReport;
import com.ooadprojectserver.restaurantmanagement.model.report.YearlyReport;
import com.ooadprojectserver.restaurantmanagement.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(APIConstant.REPORT)
public class ReportController {

    private final ReportService reportService;

    @GetMapping(APIConstant.DAILY_REVENUE)
    public ResponseEntity<APIResponse<List<DailyReport>>> getDailyReport(DailyReportRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_DAILY_REVENUE_SUCCESS,
                        reportService.searchDailyRevenue(request)
                )
        );
    }

    @GetMapping(APIConstant.MONTHLY_REVENUE)
    public ResponseEntity<APIResponse<List<MonthlyReport>>> getMonthlyReport(
            @RequestBody MonthlyReportRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_MONTHLY_REVENUE_SUCCESS,
                        reportService.getMonthlyRevenueByYear(request)
                )
        );
    }

    @GetMapping(APIConstant.YEARLY_REVENUE)
    public ResponseEntity<APIResponse<List<YearlyReport>>> getYearlyReport() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_YEARLY_REVENUE_SUCCESS,
                        reportService.getYearlyRevenue()
                )
        );
    }

    @GetMapping(APIConstant.INVENTORY_REPORT)
    public ResponseEntity<APIResponse<List<InventoryReport>>> getInventoryReport(
            InventoryReportRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_INVENTORY_REPORT_SUCCESS,
                        reportService.getInventoryReport(request)
                )
        );
    }
}
