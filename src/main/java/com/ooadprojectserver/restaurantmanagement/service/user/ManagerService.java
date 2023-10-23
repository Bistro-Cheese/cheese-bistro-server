package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.model.user.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.AssignScheduleRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.DayResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.ManagerScheduleRespone;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.ShiftResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.UserInfoResponse;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Timekeeping;
import com.ooadprojectserver.restaurantmanagement.model.schedule.TimekeepingStatus;
import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.ScheduleRepository;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.TimekeepingRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final AddressRepository addressRepository;
    private final TimekeepingRepository timekeepingRepository;
    private final ScheduleRepository scheduleRepository;

    public void createUser(UserRegisterRequest request) {
        String sDob = request.getDateOfBirth();
        Date dob;
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        managerRepository.save(Manager.managerBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(dob)
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(AccountStatus.INACTIVE.getValue())
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine(request.getAddressLine())
                                        .city(request.getCity())
                                        .region(request.getRegion())
                                        .build()
                        )
                )
                .enabled(false)
                .foreignLanguage(request.getForeignLanguage())
                .experiencedYear(request.getExperiencedYear())
                .certificationManagement(request.getCertificationManagement())
                .email(request.getEmail())
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .build());
    }

    public void updateUser(UUID user_id, UpdateProfileRequest request) {
        managerRepository.updateManager(
                request.getCertificationManagement(),
                request.getExperiencedYear(),
                request.getForeignLanguage(),
                user_id
        );
    }

    public ManagerScheduleRespone getManagerSchedule(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        return getManagerScheduleRespone(username);
    }

    public void assignSchedule(String staff_username, AssignScheduleRequest request, HttpServletRequest httpServletRequest) {
        String username = jwtService.getUsernameFromHeader(httpServletRequest);
        Optional<Timekeeping> existedTimekeeping = timekeepingRepository.findStaffSchedule(
                staff_username,
                request.getDayOfWeek(),
                request.getShift()
        );
        if (existedTimekeeping.isEmpty()) {
            Manager manager = (Manager) managerRepository.findByUsername(username).orElseThrow();
            Staff staff = (Staff) staffRepository.findByUsername(staff_username).orElseThrow();
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

    public void deleteSchedule(Long timekeeping_id) {
        Timekeeping timekeeping = timekeepingRepository.findById(timekeeping_id).orElseThrow(
                () -> new CustomException(APIStatus.SCHEDULE_NOT_FOUND)
        );
        timekeepingRepository.delete(timekeeping);
    }

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
