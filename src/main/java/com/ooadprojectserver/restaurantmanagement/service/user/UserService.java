package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    void saveUser(UserRegisterRequest userRequest);
    void updateUserById(User user, UserRegisterRequest userRequest);
    UserResponse getProfile(HttpServletRequest request);
}
