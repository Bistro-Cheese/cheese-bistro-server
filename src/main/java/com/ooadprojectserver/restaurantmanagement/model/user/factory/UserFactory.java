package com.ooadprojectserver.restaurantmanagement.model.user.factory;

import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

public interface UserFactory {
    User createUser(UserRegisterRequest registerRequest);
}
