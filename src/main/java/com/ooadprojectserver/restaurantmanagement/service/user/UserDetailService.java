package com.ooadprojectserver.restaurantmanagement.service.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserDetailService {
    UserDetails userDetails();

    UUID getIdLogin();

    String getUsernameLogin();
}
