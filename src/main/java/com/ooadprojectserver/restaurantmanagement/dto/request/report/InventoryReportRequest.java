package com.ooadprojectserver.restaurantmanagement.dto.request.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import lombok.Data;

import java.util.Date;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT2;

@Data
public class InventoryReportRequest {
    @JsonProperty("date")
    @JsonFormat(pattern = DATE_FORMAT2, timezone = DateTimeConstant.TIMEZONE)
    private Date date;
}
