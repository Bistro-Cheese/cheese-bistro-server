package com.ooadprojectserver.restaurantmanagement.dto.response.model.schedule;

import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffScheduleResponse {
    private List<Schedule> schedule;
}
