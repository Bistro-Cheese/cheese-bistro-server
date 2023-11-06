package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.AssignScheduleRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.ManagerScheduleRespone;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;
import com.ooadprojectserver.restaurantmanagement.service.schedule.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping(APIConstant.SCHEDULE)
    public ResponseEntity<APIResponse<StaffScheduleResponse>> getSchedule(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_SCHEDULE_SUCCESS,
                        scheduleService.getSchedule(request)
                )
        );
    }

    @GetMapping(APIConstant.SCHEDULE + "1")
    public ResponseEntity<APIResponse<ManagerScheduleRespone>> getWeekSchedule(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ALL_SCHEDULE_SUCCESS,
                        scheduleService.getManagerSchedule(request)
                )
        );
    }

    @PostMapping(APIConstant.SCHEDULE + APIConstant.STAFF_USERNAME)
    public ResponseEntity<MessageResponse> assignSchedule(
            @PathVariable String staff_username,
            @RequestBody AssignScheduleRequest request,
            HttpServletRequest httpServletRequest
    ) {
        scheduleService.assignSchedule(staff_username, request, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.ASSIGN_SCHEDULE_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.SCHEDULE + APIConstant.TIMEKEEPING_ID)
    public ResponseEntity<MessageResponse> deleteSchedule(
            @PathVariable Long timekeeping_id
    ) {
        scheduleService.deleteSchedule(timekeeping_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_SCHEDULE_SUCCESS)
        );
    }

    @PatchMapping(APIConstant.SCHEDULE + APIConstant.TIMEKEEPING_ID)
    public ResponseEntity<MessageResponse> timekeepingStaff(
            @PathVariable Long timekeeping_id
    ) {
        scheduleService.timekeepingStaff(timekeeping_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.TIMEKEEPING_SUCCESS)
        );
    }
}
