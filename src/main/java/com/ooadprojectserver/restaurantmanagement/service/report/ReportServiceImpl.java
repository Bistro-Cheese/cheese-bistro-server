package com.ooadprojectserver.restaurantmanagement.service.report;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyRevenueRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyRevenue;
import com.ooadprojectserver.restaurantmanagement.repository.report.DailyRevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final DailyRevenueRepository dailyRevenueRepository;

    @Override
    public List<DailyRevenue> searchDailyRevenue(DailyRevenueRequest request) {
        return dailyRevenueRepository.search(request);
    }
}
