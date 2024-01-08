package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.request.report.DailyReportRequest;
import com.ooadprojectserver.restaurantmanagement.model.report.DailyReport;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.DailyRevenueRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;
import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT;

@RequiredArgsConstructor
public class DailyRevenueRepositoryCustomImpl implements DailyRevenueRepositoryCustom {

    private final QueryRepo queryRepo;

    @Override
    public List<DailyReport> search(DailyReportRequest request) {

        SimpleDateFormat parser = new SimpleDateFormat(DATE_FORMAT);

        Map<String, Object> queryParams = new HashMap<>();

        StringBuilder query = new StringBuilder("select id, date, revenue, num_of_orders, num_of_cus ");
        query.append("from daily_revenue ");

        if (!isNullObject(request.getFromDate())){
            var fromDate = parser.format(request.getFromDate());
            query.append("where date >= :fromDate ");
            queryParams.put("fromDate", fromDate);
        }

        if (!isNullObject(request.getToDate())) {
            var toDate = parser.format(request.getToDate());
            query.append("and date <= :toDate ");
            queryParams.put("toDate", toDate);
        }

        query.append("order by id asc ");

        return queryRepo.queryList(query.toString(), queryParams, DailyReport.class);
    }
}
