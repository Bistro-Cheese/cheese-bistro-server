package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserRegisterRequest userRequest);
}
