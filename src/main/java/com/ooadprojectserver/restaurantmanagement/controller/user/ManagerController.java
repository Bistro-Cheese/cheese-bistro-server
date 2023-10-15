package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.AssignScheduleRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.schedule.ManagerScheduleRespone;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.MANAGER)
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping(APIConstant.SCHEDULE)
    public ResponseEntity<APIResponse<ManagerScheduleRespone>> getWeekSchedule(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ALL_SCHEDULE_SUCCESS,
                        managerService.getManagerSchedule(request)
                )
        );
    }

    @PostMapping(APIConstant.SCHEDULE + APIConstant.STAFF_USERNAME)
    public ResponseEntity<MessageResponse> assignSchedule(
            @PathVariable String staff_username,
            @RequestBody AssignScheduleRequest request,
            HttpServletRequest httpServletRequest
            ) {
        managerService.assignSchedule(staff_username, request, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.ASSIGN_SCHEDULE_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.SCHEDULE + APIConstant.TIMEKEEPING_ID)
    public ResponseEntity<MessageResponse> deleteSchedule(
            @PathVariable Long timekeeping_id
    ) {
        managerService.deleteSchedule(timekeeping_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_SCHEDULE_SUCCESS)
        );
    }

    @PatchMapping(APIConstant.SCHEDULE + APIConstant.TIMEKEEPING_ID)
    public ResponseEntity<MessageResponse> timekeepingStaff(
            @PathVariable Long timekeeping_id
    ) {
        managerService.timekeepingStaff(timekeeping_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.TIMEKEEPING_SUCCESS)
        );
    }
}
