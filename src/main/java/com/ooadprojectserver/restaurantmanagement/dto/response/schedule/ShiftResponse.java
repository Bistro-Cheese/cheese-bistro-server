package com.ooadprojectserver.restaurantmanagement.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftResponse {
    private Shift shift;
    @JsonProperty("staff_count")
    private int staffCount;
    @JsonProperty("staff_list")
    private List<UserInfoResponse> staffList;
}
