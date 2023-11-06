package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;

public interface OwnerService extends UserService {
    void createUser(UserRegisterRequest userRequest);
}
