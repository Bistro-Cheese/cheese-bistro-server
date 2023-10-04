package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

import java.util.List;

public interface UserService {
    public User createUser(UserRegisterRequest request);
}
