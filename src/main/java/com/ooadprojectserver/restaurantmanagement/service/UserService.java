package com.ooadprojectserver.restaurantmanagement.service;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;

public interface UserService {
    void login(UserLoginRequest request);
}
