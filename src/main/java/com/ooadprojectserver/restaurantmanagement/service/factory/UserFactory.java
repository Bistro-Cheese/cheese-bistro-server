package com.ooadprojectserver.restaurantmanagement.service.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

public interface UserFactory {
    User createUser(UserRegisterRequest registerRequest);
}
