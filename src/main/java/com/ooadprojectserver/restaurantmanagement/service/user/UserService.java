package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.constant.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.constant.RoleConstant.*;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.UserResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.type.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.TimekeepingRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.specification.UserSpecification;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TimekeepingRepository timekeepingRepository;
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

    public void deleteUser(UUID user_id) throws CustomException {
        User user = userRepository.findById(user_id).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        switch (user.getRole()) {
            case 1 -> timekeepingRepository.deleteByStaff((Staff) user);
            case 2 -> timekeepingRepository.deleteByManager((Manager) user);
            case 3 -> throw new CustomException(APIStatus.ERR_DELETE_OWNER);
        }
        userRepository.delete(user);
    }

    public void updateUser(UpdateProfileRequest updateRequestBody, UUID user_id) {
        User user = this.userRepository.findById(user_id).orElseThrow();
        Date lastModifiedDate = new Date();

        //  Covert String to Date
        String sDob = updateRequestBody.getDateOfBirth();
        Date dob;
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
        String username = jwtService.getUsernameFromHeader(request);
        if (username == null) {
            throw new NoSuchElementException();
        }
        User user = this.userRepository.findByUsername(username).orElseThrow();
        return this.covertUserToUserResponse(user);
    }

    public PagingResponseModel searchUser(String name, String role, String sort, int pageNumber, int pageSize) {
        if (pageSize < 1 || pageNumber < 1) {
            throw new CustomException(APIStatus.ERR_PAGINATION);
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<User> userPage = userRepository.findAll(new UserSpecification(name, role, sort), pageable);
        List<UserResponse> userResponseList = new ArrayList<>();
        if (!userPage.isEmpty()) {
            for (User user : userPage) {
                userResponseList.add(this.covertUserToUserResponse(user));
            }
        }
        Page<UserResponse> userResponsePage = new PageImpl<>(userResponseList);
        return new PagingResponseModel(
                userResponsePage.getContent(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber() + 1
        );
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
            .id(user.getId())
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
