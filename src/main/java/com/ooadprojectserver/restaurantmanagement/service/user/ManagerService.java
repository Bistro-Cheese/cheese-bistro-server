package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;

import java.util.List;

public interface ManagerService extends UserService {
    List<UserResponse> getUsers();
}
