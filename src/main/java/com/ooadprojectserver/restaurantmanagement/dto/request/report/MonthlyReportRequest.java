package com.ooadprojectserver.restaurantmanagement.dto.request.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MonthlyReportRequest {
    @JsonProperty("year")
    private Integer year;
}