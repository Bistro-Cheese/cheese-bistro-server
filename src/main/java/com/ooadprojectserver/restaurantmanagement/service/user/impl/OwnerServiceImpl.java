package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Status;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.specification.UserSpecification;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.OwnerFactory;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerFactory ownerFactory;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final Map<Integer, UserService> roleToServiceMap;

    public OwnerServiceImpl(
            OwnerFactory ownerFactory,
            UserRepository userRepository,
            OwnerRepository ownerRepository,
            ManagerService managerService,
            StaffService staffService,
            JwtService jwtService,
            EmailService emailService,
            Map<Integer, UserService> roleToServiceMap
    ) {
        this.ownerFactory = ownerFactory;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.managerService = managerService;
        this.staffService = staffService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.roleToServiceMap = roleToServiceMap;
    }

    @PostConstruct
    private void initRoleToServiceMap() {
        roleToServiceMap.put(1, this);
        roleToServiceMap.put(2, managerService);
        roleToServiceMap.put(3, staffService);
    }

    // Implement User Service Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        ownerRepository.save(ownerFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        ownerRepository.save(ownerFactory.update(user, userRegisterRequest));
    }

    @Override
    public UserResponse getProfile(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        User owner = ownerRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return covertUserToUserResponse(owner);
    }
    // Implement User Service End

    // Implement Owner Service Start
    @Override
    public void createUser(UserRegisterRequest userRegisterRequest) {
        // Check if email already existed
        userRepository.findByEmail(userRegisterRequest.getEmail()).ifPresent(user -> {
            throw new CustomException(APIStatus.EMAIL_ALREADY_EXISTED);
        });

        // Check if username already existed
        userRepository.findByUsername(userRegisterRequest.getUsername()).ifPresent(user -> {
            throw new CustomException(APIStatus.USERNAME_ALREADY_EXISTED);
        });

        // Check if phone number already existed
        userRepository.findByPhoneNumber(userRegisterRequest.getPhoneNumber()).ifPresent(user -> {
            throw new CustomException(APIStatus.PHONE_NUMBER_ALREADY_EXISTED);
        });

        // Send confirmation email
        ConfirmationRequest confirm = ConfirmationRequest.builder()
                .fullName(userRegisterRequest.getFirstName() + " " + userRegisterRequest.getLastName())
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .emailTo(userRegisterRequest.getEmail())
                .build();
        emailService.sendMailWithInline(confirm, Locale.ENGLISH);

        // Save user
        UserService userService = roleToServiceMap.get(userRegisterRequest.getRole());
        if (userService == null) {
            throw new CustomException(APIStatus.INVALID_ROLE_ID);
        } else {
            userService.saveUser(userRegisterRequest);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(covertUserToUserResponse(user));
        }
        return userResponses;
    }

    @Override
    public UserResponse getUserDetail(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        return covertUserToUserResponse(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUser(UUID userId, UserRegisterRequest userRegisterRequest) {
        // Check User Id
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );

        // Check if role is changed
        if (!Objects.equals(userRegisterRequest.getRole(), user.getRole())) {
            throw new CustomException(APIStatus.ROLE_CANNOT_UPDATE);
        }

        // Check if email already existed
        if (!Objects.equals(userRegisterRequest.getEmail(), user.getEmail())) {
            userRepository.findByEmail(userRegisterRequest.getEmail()).ifPresent(userRepo -> {
                throw new CustomException(APIStatus.EMAIL_ALREADY_EXISTED);
            });
        }

        UserService userService = roleToServiceMap.get(userRegisterRequest.getRole());
        if (userService == null) {
            throw new CustomException(APIStatus.INVALID_ROLE_ID);
        }

        // Update User
        userService.updateUserById(user, userRegisterRequest);
    }

    public PagingResponse searchUser(SearchRequest searchRequest) {
        searchRequest.getPagingRequest().checkValidPaging();
        Page<User> userPage = userRepository.findAll(
                new UserSpecification(searchRequest.getParams()),
                searchRequest.getPagingRequest().toPageRequest()
        );
        List<UserResponse> userResponseList = new ArrayList<>();
        if (!userPage.isEmpty()) {
            for (User user : userPage) {
                userResponseList.add(this.covertUserToUserResponse(user));
            }
        }
        Page<UserResponse> userResponsePage = new PageImpl<>(userResponseList);
        return new PagingResponse(
                userResponsePage.getContent(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber() + 1
        );
    }
    // Implement Owner Service End

    private UserResponse covertUserToUserResponse(User user) {
        String sRole = switch (user.getRole()) {
            case 1 -> RoleConstant.ROLE.OWNER.name().toLowerCase();
            case 2 -> RoleConstant.ROLE.MANAGER.name().toLowerCase();
            case 3 -> RoleConstant.ROLE.STAFF.name().toLowerCase();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };
        Integer status = switch (user.getStatus()) {
            case 0 -> Status.ACTIVE.ordinal();
            case 1 -> Status.INACTIVE.ordinal();
            default -> throw new IllegalStateException("Unexpected value: " + user.getStatus());
        };
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .status(status)
                .role(sRole)
                .build();
    }
}
