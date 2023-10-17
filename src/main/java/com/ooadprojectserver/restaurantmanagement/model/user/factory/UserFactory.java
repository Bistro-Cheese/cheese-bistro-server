package com.ooadprojectserver.restaurantmanagement.model.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.type.User;

public interface UserFactory {
    void createUser(UserRegisterRequest registerRequest);

    void updateUser(User user, UpdateProfileRequest updateProfileRequest);
}
