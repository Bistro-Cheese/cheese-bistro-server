package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.ManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerFactory managerFactory;
    private final ManagerRepository managerRepository;
    private final JwtService jwtService;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.update(user, userRegisterRequest));
    }

    @Override
    public UserResponse getProfile(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        User manager = managerRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return UserResponse.covertUserToUserResponse(manager);
    }
    // UserService implementation End
}
