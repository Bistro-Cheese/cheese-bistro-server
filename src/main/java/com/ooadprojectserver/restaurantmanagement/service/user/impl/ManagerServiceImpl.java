package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Role;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.ManagerFactory;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerFactory managerFactory;
    private final UserRepository userRepository;
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    // UserService implementation Start
    @Override
    public void saveUser(UserCreateRequest userCreateRequest) {
        UserFactory factory = new ManagerFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User manager = factory.create(userCreateRequest);
        userRepository.save(manager);
    }

    @Override
    public void updateUserById(User user, UserCreateRequest userCreateRequest) {
        UserFactory factory = new ManagerFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User manager = factory.update(user, userCreateRequest);
        userRepository.save(manager);
    }

    @Override
    public UserResponse getProfile() {
        String username = userDetailService.getUsernameLogin();
        Manager manager = (Manager) userRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.error(APIStatus.USER_NOT_FOUND.getMessage());
                    return new CustomException(APIStatus.USER_NOT_FOUND);
                }
        );
        return UserResponse.covertUserToUserResponse(manager);
    }

    @Override
    public Manager getUserById(UUID id) {
        return (Manager) userRepository.findById(id).orElseThrow(
                () -> {
                    log.error(APIStatus.USER_NOT_FOUND.getMessage());
                    return new CustomException(APIStatus.USER_NOT_FOUND);
                }
        );
    }
    // UserService implementation End

    // ManagerService implementation Start
    @Override
    public List<UserResponse> getUsers() {
        List<User> staffs = userRepository.findByRole(Role.STAFF);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User staff : staffs) {
            userResponses.add(UserResponse.covertUserToUserResponse(staff));
        }
        return userResponses;
    }
    // ManagerService implementation End
}
