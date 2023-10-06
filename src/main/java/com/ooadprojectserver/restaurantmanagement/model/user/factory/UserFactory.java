package com.ooadprojectserver.restaurantmanagement.model.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

public interface UserFactory {
    User createUser(UserRegisterRequest registerRequest);

    void updateUser(User user, UpdateProfileRequest updateProfileRequest);
}
