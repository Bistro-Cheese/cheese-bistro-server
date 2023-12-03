package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyRevenueRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyRevenue;

import java.util.List;

public interface DailyRevenueRepositoryCustom {
    List<DailyRevenue> search(DailyRevenueRequest request);
}
