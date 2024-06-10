package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.StaffFactory;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    // UserService implementation Start
    @Override
    public void saveUser(UserCreateRequest userCreateRequest) {
        UserFactory factory = new StaffFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User staff = factory.create(userCreateRequest);
        log.info(staff.toString());
        userRepository.save(staff);
    }

    @Override
    public void updateUserById(User user, UserCreateRequest userCreateRequest) {
        UserFactory factory = new StaffFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User staff = factory.update(user, userCreateRequest);
        userRepository.save(staff);
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
    public Staff getUserById(UUID id) {
        return (Staff) userRepository.findById(id).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
    }
    // UserService implementation End
}

