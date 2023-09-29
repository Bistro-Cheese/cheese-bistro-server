package com.ooadprojectserver.restaurantmanagement.service.factory.implement;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {
    private final StaffService staffService;
    private final ManagerService managerService;
    private final OwnerService ownerService;

    @Override
    public User createUser(UserRegisterRequest request) {
        return switch (request.getRole()) {
            case STAFF -> staffService.createUser(request);
            case MANAGER -> managerService.createUser(request);
            case OWNER -> ownerService.createUser(request);
            default -> throw new RuntimeException();
        };
    }
}
