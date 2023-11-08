package com.ooadprojectserver.restaurantmanagement.service.schedule.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.AssignScheduleRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.*;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Timekeeping;
import com.ooadprojectserver.restaurantmanagement.model.schedule.TimekeepingStatus;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.ScheduleRepository;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.TimekeepingRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.schedule.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final JwtService jwtService;
    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;
    private final TimekeepingRepository timekeepingRepository;

    @Override
    public ManagerScheduleRespone getManagerSchedule(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        return getManagerScheduleRespone(username);
    }

    @Override
    public void assignSchedule(UUID staffId, AssignScheduleRequest request, HttpServletRequest httpServletRequest) {
        String username = jwtService.getUsernameFromHeader(httpServletRequest);
        Optional<Timekeeping> existedTimekeeping = timekeepingRepository.findStaffSchedule(
                staffId,
                request.getDayOfWeek(),
                request.getShift()
        );
        if (existedTimekeeping.isEmpty()) {
            Manager manager = (Manager) managerRepository.findByUsername(username).orElseThrow();
            Staff staff = (Staff) staffRepository.findById(staffId).orElseThrow();
            Schedule schedule = scheduleRepository.findByDayAndShift(request.getDayOfWeek(), request.getShift());

            Timekeeping timeKeeping = Timekeeping.builder()
                    .manager(manager)
                    .staff(staff)
                    .schedule(schedule)
                    .workDate(null)
                    .status(null)
                    .build();
            timekeepingRepository.save(timeKeeping);
        } else {
            throw new CustomException(APIStatus.SCHEDULE_ALREADY_EXISTED);
        }
    }

    @Override
    public void deleteSchedule(Long timekeeping_id) {
        Timekeeping timekeeping = timekeepingRepository.findById(timekeeping_id).orElseThrow(
                () -> new CustomException(APIStatus.SCHEDULE_NOT_FOUND)
        );
        timekeepingRepository.delete(timekeeping);
    }

    @Override
    public void timekeepingStaff(Long timekeeping_id) {
        Timekeeping timekeeping = timekeepingRepository.findById(timekeeping_id).orElseThrow(
                () -> new CustomException(APIStatus.SCHEDULE_NOT_FOUND)
        );

        if (timekeeping.getStatus() != null) {
            return;
        }

        LocalDateTime timekeepingDate = LocalDateTime.now(ZoneId.of(DateTimeConstant.TIMEZONE));
        int timekeepingStatus;
        Shift shift = timekeeping.getSchedule().getShift();

        if (timekeepingDate.getHour() >= shift.getEndTime()) {
            timekeepingStatus = TimekeepingStatus.ABSENT.getValue();
        } else if (timekeepingDate.getHour() >= shift.getStartTime() && timekeepingDate.getMinute() != 0) {
            timekeepingStatus = TimekeepingStatus.LATE.getValue();
        } else {
            timekeepingStatus = TimekeepingStatus.ON_TIME.getValue();
        }

        timekeepingRepository.updateWorkDateAndStatusById(timekeepingDate, timekeepingStatus, timekeeping_id);
    }

    @Override
    public StaffScheduleResponse getSchedule(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        List<Timekeeping> timekeepingList = timekeepingRepository.findStaffSchedule(username);
        List<Schedule> scheduleList = new ArrayList<>();
        for (Timekeeping timekeeping : timekeepingList) {
            scheduleList.add(timekeeping.getSchedule());
        }

        Comparator<Schedule> comparator = Comparator
                .comparing(Schedule::getDay)
                .thenComparing(Schedule::getShift);

        scheduleList.sort(comparator);

        return StaffScheduleResponse.builder()
                .schedule(scheduleList)
                .build();
    }

    private ManagerScheduleRespone getManagerScheduleRespone(
            String username
    ) {
        List<DayResponse> dayResponseList = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            dayResponseList.add(this.getDayResponse(username, day));
        }

        return ManagerScheduleRespone.builder()
                .schedule(dayResponseList)
                .build();
    }

    private DayResponse getDayResponse(
            String username,
            DayOfWeek day
    ) {
        List<ShiftResponse> shiftResponseList = new ArrayList<>();

        for (Shift shift : Shift.values()) {
            shiftResponseList.add(this.getShiftResponse(username, day, shift));
        }

        return DayResponse.builder()
                .day(day)
                .shiftList(shiftResponseList)
                .build();
    }

    private ShiftResponse getShiftResponse(
            String username,
            DayOfWeek day,
            Shift shift
    ) {
        List<Timekeeping> timekeepingList = timekeepingRepository.findManagerSchedule(username, day, shift);

        List<UserInfoResponse> staffList = new ArrayList<>();
        for (Timekeeping timekeeping : timekeepingList) {
            Staff staff = timekeeping.getStaff();
            staffList.add(
                    UserInfoResponse.builder()
                            .scheduleId(timekeeping.getId())
                            .username(staff.getUsername())
                            .firstName(staff.getFirstName())
                            .lastName(staff.getLastName())
                            .phoneNumber(staff.getPhoneNumber())
                            .build()
            );
        }

        return ShiftResponse.builder()
                .shift(shift)
                .staffCount(staffList.size())
                .staffList(staffList)
                .build();
    }
}
