package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.model.user.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Timekeeping;
import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.TimekeepingRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final StaffRepository staffRepository;
    private final AddressRepository addressRepository;
    private final TimekeepingRepository timekeepingRepository;

    public void createUser(UserRegisterRequest request) {
        String sDob = request.getDateOfBirth();
        Date dob;
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        staffRepository.save(Staff.staffBuilder()
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
                .academicLevel(request.getAcademicLevel())
                .foreignLanguage(request.getForeignLanguage())
                .email(request.getEmail())
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(false)
                .build());
    }

    public void updateUser(UUID user_id, UpdateProfileRequest request) {
        staffRepository.updateStaff(
                request.getAcademicLevel(),
                request.getForeignLanguage(),
                user_id
        );
    }

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
}
