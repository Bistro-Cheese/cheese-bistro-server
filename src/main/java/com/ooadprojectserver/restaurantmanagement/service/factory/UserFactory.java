package com.ooadprojectserver.restaurantmanagement.service.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.util.Constant;

import java.util.Optional;

public interface UserFactory {
    User createUser(UserRegisterRequest userRegister);
}
