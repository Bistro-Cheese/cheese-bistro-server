package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.constant.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant.*;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.UserResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.repository.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthenticationService authenticationService;
    private final UserFactory userFactory;
    private final JwtService jwtService;

    public List<UserResponse> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        userList.forEach(user -> {
            UserResponse userResponse = this.covertUserToUserResponse(user);
            userResponseList.add(userResponse);
        });
        return userResponseList;
    }

    public void deleteUser(UUID user_id) {
        this.userRepository.deleteById(user_id);
    }

    public void updateUser(UpdateProfileRequest updateRequestBody, UUID user_id) {
        User user = this.userRepository.findById(user_id).orElseThrow();
        Date lastModifiedDate = new Date();

        //  Covert String to Date
        String sDob = updateRequestBody.getDateOfBirth();
        Date dob = new Date();
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //  Update Address
        addressRepository.updateAddressById(
                updateRequestBody.getAddressLine(),
                updateRequestBody.getCity(),
                updateRequestBody.getRegion(),
                user.getAddress().getId()
        );

        // Update User
        userRepository.updateUserById(
                updateRequestBody.getFirstName(),
                updateRequestBody.getLastName(),
                lastModifiedDate,
                dob,
                updateRequestBody.getPhoneNumber(),
                user.getId()
        );

        userFactory.updateUser(user, updateRequestBody);
    }

    public UserResponse getProfile(HttpServletRequest request) {
        String accessToken = authenticationService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(accessToken);
        if (username == null) {
            throw new NoSuchElementException();
        }
        User user = this.userRepository.findByUsername(username).orElseThrow();
        return this.covertUserToUserResponse(user);
    }

    private UserResponse covertUserToUserResponse(User user) {
        String sRole = switch (user.getRole()) {
            case 1 -> ROLE.STAFF.name().toLowerCase();
            case 2 -> ROLE.MANAGER.name().toLowerCase();
            case 3 -> ROLE.OWNER.name().toLowerCase();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };
        String sStatus = switch (user.getStatus()) {
            case 1 -> AccountStatus.ACTIVE_STATUS.getType();
            case 2 -> AccountStatus.DELETED_STATUS.getType();
            case 3 -> AccountStatus.REVOKE_STATUS.getType();
            case 4 -> AccountStatus.DISABLED_STATUS.getType();
            case 5 -> AccountStatus.DELETED_FOREVER_STATUS.getType();
            case 6 -> AccountStatus.PENDING.getType();
            default -> throw new IllegalStateException("Unexpected value: " + user.getStatus());
        };
        return UserResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .status(sStatus)
                .role(sRole)
                .build();
    }
}
