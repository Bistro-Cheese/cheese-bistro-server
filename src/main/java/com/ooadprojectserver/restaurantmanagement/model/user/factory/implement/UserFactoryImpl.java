package com.ooadprojectserver.restaurantmanagement.model.user.factory.implement;

import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {
    private final StaffService staffService;
    private final ManagerService managerService;
    private final OwnerService ownerService;
    private final UserRepository userRepository;

    @Override
    public User createUser(UserRegisterRequest request) {
        return switch (request.getRole()) {
            case STAFF -> staffService.createUser(request);
            case MANAGER -> managerService.createUser(request);
            case OWNER -> ownerService.createUser(request);
            default -> throw new RuntimeException();
        };
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
}
