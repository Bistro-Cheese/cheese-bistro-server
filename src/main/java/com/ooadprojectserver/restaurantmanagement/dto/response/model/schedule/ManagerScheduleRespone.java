package com.ooadprojectserver.restaurantmanagement.dto.response.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerScheduleRespone {
    private List<DayResponse> schedule;
}
