package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.STAFF)
@PreAuthorize("hasRole('STAFF')")
public class StaffController {
    private final StaffService staffService;

    @GetMapping(APIConstant.SCHEDULE)
    public ResponseEntity<APIResponse<StaffScheduleResponse>> getSchedule(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_SCHEDULE_SUCCESS,
                        staffService.getSchedule(request)
                )
        );
    }
}
