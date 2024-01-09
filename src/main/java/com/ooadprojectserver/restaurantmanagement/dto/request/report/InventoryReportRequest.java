package com.ooadprojectserver.restaurantmanagement.dto.request.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_FORMAT2;

@Data
public class InventoryReportRequest {
    @JsonProperty("date")
    @DateTimeFormat(pattern = DATE_FORMAT2)
    private LocalDate date;
}
