package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.StaffFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffFactory staffFactory;
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    // UserService implementation Start
    @Override
    public void saveUser(UserCreateRequest userCreateRequest) {
        userRepository.save(staffFactory.create(userCreateRequest));
    }

    @Override
    public void updateUserById(User user, UserCreateRequest userCreateRequest) {
        userRepository.save(staffFactory.update(user, userCreateRequest));
    }

    @Override
    public UserResponse getProfile() {
        String username = userDetailService.getUsernameLogin();
        Staff staff = (Staff) userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return UserResponse.covertUserToUserResponse(staff);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
    }
    // UserService implementation End
}

