package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface OwnerService extends UserService {
    void createUser(UserRegisterRequest userRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUserDetail(UUID userId);
    void deleteUser(UUID userId);
    void updateUser(UUID userId, UserRegisterRequest userRequest);
    PagingResponse searchUser(SearchRequest searchRequest);
}
