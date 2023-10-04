package com.ooadprojectserver.restaurantmanagement.model.user.factory;

import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

import java.util.List;

public interface UserFactory {
    User createUser(UserRegisterRequest registerRequest);

    List<User> getUsers();
}
