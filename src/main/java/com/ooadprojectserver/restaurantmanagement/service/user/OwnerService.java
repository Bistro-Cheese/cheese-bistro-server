package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;

import java.util.List;
import java.util.UUID;

public interface OwnerService extends UserService {
    void createUser(UserCreateRequest userRequest);
    List<UserResponse> getUsers();
    User getUserDetail(UUID userId);
    void deleteUser(UUID userId);
    void updateUser(UUID userId, UserCreateRequest userRequest);
    PagingResponse searchUser(SearchRequest searchRequest);
}
