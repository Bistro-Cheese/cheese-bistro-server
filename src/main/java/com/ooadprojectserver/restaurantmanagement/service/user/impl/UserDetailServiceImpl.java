package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    public UserDetails userDetails() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal);
        } else {
            return null;
        }
    }

    public UUID getIdLogin() {
        UserDetails userDetails = userDetails();
        if (userDetails == null) {
            return null;
        }
        return ((User) userDetails).getId();
    }

    public String getUsernameLogin() {
        User userDetails = (User) userDetails();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getUsername();
    }
}
