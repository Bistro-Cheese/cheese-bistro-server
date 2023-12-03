package com.ooadprojectserver.restaurantmanagement.dto.request.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT;

@Data
public class DailyRevenueRequest {
    @JsonProperty("fromDate")
    @JsonFormat(pattern = DATE_FORMAT)
    private Date fromDate;

    @JsonProperty("toDate")
    @JsonFormat(pattern = DATE_FORMAT)
    private Date toDate;

}
