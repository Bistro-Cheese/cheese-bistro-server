package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;

import java.util.UUID;

public interface UserService {
    void saveUser(UserCreateRequest userRequest);
    void updateUserById(User user, UserCreateRequest userRequest);
    UserResponse getProfile();
    User getUserById(UUID id);
}
