package com.ooadprojectserver.restaurantmanagement.auth.factory;

import com.ooadprojectserver.restaurantmanagement.auth.model.AuthUser;
import com.ooadprojectserver.restaurantmanagement.model.user.User;

public interface AuthUserFactory {
    AuthUser createAuthUser (User user);
}
