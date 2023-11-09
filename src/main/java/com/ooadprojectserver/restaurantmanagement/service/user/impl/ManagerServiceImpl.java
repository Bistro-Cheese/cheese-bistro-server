package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Role;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.ManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerFactory managerFactory;
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        userRepository.save(managerFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        userRepository.save(managerFactory.update(user, userRegisterRequest));
    }

    @Override
    public UserResponse getProfile() {
        String username = userDetailService.getUsernameLogin();
        Manager manager = (Manager) userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return UserResponse.covertUserToUserResponse(manager);
    }
    // UserService implementation End

    // ManagerService implementation Start
    @Override
    public List<UserResponse> getAllStaffs() {
        List<User> staffs = userRepository.findByRole(Role.STAFF);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User staff : staffs) {
            userResponses.add(UserResponse.covertUserToUserResponse(staff));
        }
        return userResponses;
    }
    // ManagerService implementation End
}
