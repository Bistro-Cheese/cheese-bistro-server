package com.ooadprojectserver.restaurantmanagement.service.implement;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;

    @Override
    public User createUser(UserRegisterRequest request) {
        return managerRepository.save(Manager.managerBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole().getValue())
                .status(request.getStatus())
                .createAt(LocalDateTime.now())
                .foreignLanguage(request.getForeignLanguage())
                .experiencedYear(request.getExperiencedYear())
                .certificationManagement(request.getCertificationManagement())
                .build());
    }
}
