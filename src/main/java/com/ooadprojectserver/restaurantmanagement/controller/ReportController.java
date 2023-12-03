package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyRevenueRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyRevenue;
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
    public ResponseEntity<APIResponse<List<DailyRevenue>>> getDailyRevenueReport(
            @RequestBody DailyRevenueRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_DAILY_REVENUE_SUCCESS,
                        reportService.searchDailyRevenue(request)
                )
        );
    }
}
