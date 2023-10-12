package com.ooadprojectserver.restaurantmanagement.model.user.factory.implement;

import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.type.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;

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
            case OWNER -> ownerService.createUser(request);
            case MANAGER -> managerService.createUser(request);
            case STAFF -> staffService.createUser(request);
            default -> throw new RuntimeException();
        };
    }

    @Override
    public void updateUser(User user, UpdateProfileRequest request) {
        switch (user.getRole()) {
            case 1-> staffService.updateUser(user.getId() ,request);
            case 2 -> managerService.updateUser(user.getId(), request);
            case 3 -> ownerService.updateUser(user.getId(), request);
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };
    }
}
