package com.ooadprojectserver.restaurantmanagement.service;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User createUser(UserRegisterRequest request);
}
