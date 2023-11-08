package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.StaffFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffFactory staffFactory;
    private final StaffRepository staffRepository;
    private final JwtService jwtService;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        staffRepository.save(staffFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        staffRepository.save(staffFactory.update(user, userRegisterRequest));
    }

    @Override
    public UserResponse getProfile(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        User staff = staffRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return UserResponse.covertUserToUserResponse(staff);
    }
    // UserService implementation End
}

