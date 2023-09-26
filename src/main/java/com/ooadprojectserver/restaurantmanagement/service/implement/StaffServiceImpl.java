package com.ooadprojectserver.restaurantmanagement.service.implement;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final StaffRepository staffRepository;
    @Override
    public User createUser(UserRegisterRequest request) {
        return  staffRepository.save(Staff.staffBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole().getValue())
                .status(request.getStatus())
                .createAt(LocalDateTime.now())
                .academicLevel(request.getAcademicLevel())
                .foreignLanguage(request.getForeignLanguage())
                .build());
    }
}
