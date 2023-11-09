package com.ooadprojectserver.restaurantmanagement.service.schedule;

import com.ooadprojectserver.restaurantmanagement.dto.request.AssignScheduleRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.ManagerScheduleRespone;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;

import java.util.UUID;

public interface ScheduleService {
    StaffScheduleResponse getSchedule();
    ManagerScheduleRespone getManagerSchedule();
    void assignSchedule(UUID staffId, AssignScheduleRequest request);
    void deleteSchedule(Long timekeeping_id);
    void timekeepingStaff(Long timekeeping_id);
}
