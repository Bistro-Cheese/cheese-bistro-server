package com.ooadprojectserver.restaurantmanagement.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DayResponse {
    @JsonProperty("day")
    private DayOfWeek day;
    @JsonProperty("shift_list")
    private List<ShiftResponse> shiftList;
}
