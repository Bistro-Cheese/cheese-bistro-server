package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.specification.UserSpecification;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import com.ooadprojectserver.restaurantmanagement.service.email.command.EmailCommand;
import com.ooadprojectserver.restaurantmanagement.service.email.command.SendMailWithInlineCommand;
import com.ooadprojectserver.restaurantmanagement.service.user.*;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.OwnerFactory;
import jakarta.annotation.PostConstruct;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.UserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {
    private final UserRepository userRepository;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final UserDetailService userDetailService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    private Map<Integer, UserService> roleToServiceMap;

    public OwnerServiceImpl(UserRepository userRepository, ManagerService managerService, StaffService staffService, UserDetailService userDetailService, EmailService emailService, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
            this.userRepository = userRepository;
            this.managerService = managerService;
            this.staffService = staffService;
            this.userDetailService = userDetailService;
            this.passwordEncoder = passwordEncoder;
            this.addressRepository = addressRepository;
            this.emailService = emailService;
        }

    @PostConstruct
    private void initRoleToServiceMap() {
        roleToServiceMap = new HashMap<>();
        roleToServiceMap.put(0, this);
        roleToServiceMap.put(1, managerService);
        roleToServiceMap.put(2, staffService);
    }

    // Implement User Service Start
    @Override
    public void saveUser(UserCreateRequest userRegisterRequest) {
        UserFactory factory = new OwnerFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User owner = factory.create(userRegisterRequest);
        userRepository.save(owner);
    }

    @Override
    public void updateUserById(User user, UserCreateRequest userRegisterRequest) {
        UserFactory factory = new OwnerFactory(
                passwordEncoder,
                addressRepository,
                userDetailService
        );
        User owner = factory.update(user, userRegisterRequest);
        userRepository.save(owner);
    }

    @Override
    public UserResponse getProfile() {
        String username = userDetailService.getUsernameLogin();
        Owner owner = (Owner) userRepository.findByUsername(username).orElseThrow(() -> new CustomException(APIStatus.USER_NOT_FOUND));
        return UserResponse.covertUserToUserResponse(owner);
    }

    @Override
    public Owner getUserById(UUID id) {
        return (Owner) userRepository.findById(id).orElseThrow(() -> new CustomException(APIStatus.USER_NOT_FOUND));
    }
    // Implement User Service End

    // Implement Owner Service Start
    @Override
    public void createUser(UserCreateRequest userRegisterRequest) {
//         Check if email already existed
        userRepository.findByEmail(userRegisterRequest.getEmail()).ifPresent(user -> {
            log.error(APIStatus.EMAIL_ALREADY_EXISTED.getMessage());
            throw new CustomException(APIStatus.EMAIL_ALREADY_EXISTED);
        });

//         Check if username already existed
        userRepository.findByUsername(userRegisterRequest.getUsername()).ifPresent(user -> {
            throw new CustomException(APIStatus.USERNAME_ALREADY_EXISTED);
        });

//         Check if phone number already existed
        userRepository.findByPhoneNumber(userRegisterRequest.getPhoneNumber()).ifPresent(user -> {
            throw new CustomException(APIStatus.PHONE_NUMBER_ALREADY_EXISTED);
        });

//         Send confirmation email
        ConfirmationRequest confirm = ConfirmationRequest.builder().fullName(userRegisterRequest.getFirstName() + " " + userRegisterRequest.getLastName()).username(userRegisterRequest.getUsername()).password(userRegisterRequest.getPassword()).emailTo(userRegisterRequest.getEmail()).build();

        // Save user
        UserService userService = roleToServiceMap.get(userRegisterRequest.getRole());
        if (userService == null) {
            throw new CustomException(APIStatus.INVALID_ROLE_ID);
        } else {
            userService.saveUser(userRegisterRequest);
            EmailCommand  emailCommand = new SendMailWithInlineCommand(emailService, confirm, Locale.ENGLISH);
            emailCommand.execute();
        }
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(UserResponse.covertUserToUserResponse(user));
        }
        return userResponses;
    }

    @Override
    public User getUserDetail(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(APIStatus.USER_NOT_FOUND));
        return switch (user.getRole().ordinal()) {
            case 0 -> getUserById(userId);
            case 1 -> managerService.getUserById(userId);
            case 2 -> staffService.getUserById(userId);
            default -> throw new CustomException(APIStatus.INVALID_ROLE_ID);
        };
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error(APIStatus.USER_NOT_FOUND.getMessage());
            return new CustomException(APIStatus.USER_NOT_FOUND);
        });
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUser(UUID userId, UserCreateRequest userRegisterRequest) {
        log.error("Hello");
        // Check User Id
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(APIStatus.USER_NOT_FOUND.getMessage());
            return new CustomException(APIStatus.USER_NOT_FOUND);
        });

        // Check if email already existed
        if (!Objects.equals(userRegisterRequest.getEmail(), user.getEmail())) {
            userRepository.findByEmail(userRegisterRequest.getEmail()).ifPresent(userRepo -> {
                log.error(APIStatus.EMAIL_ALREADY_EXISTED.getMessage());
                throw new CustomException(APIStatus.EMAIL_ALREADY_EXISTED);
            });
        }

        UserService userService = roleToServiceMap.get(userRegisterRequest.getRole());
        if (userService == null) {
            log.error(APIStatus.INVALID_ROLE_ID.getMessage());
            throw new CustomException(APIStatus.INVALID_ROLE_ID);
        }

        userService.updateUserById(user, userRegisterRequest);
    }

    public PagingResponse searchUser(SearchRequest searchRequest) {
        searchRequest.getPagingRequest().checkValidPaging();
        Page<User> userPage = userRepository.findAll(new UserSpecification(searchRequest.getParams()), searchRequest.getPagingRequest().toPageRequest());
        List<UserResponse> userResponseList = new ArrayList<>();
        if (!userPage.isEmpty()) {
            for (User user : userPage) {
                userResponseList.add(UserResponse.covertUserToUserResponse(user));
            }
        }
        Page<UserResponse> userResponsePage = new PageImpl<>(userResponseList);
        return new PagingResponse(userResponsePage.getContent(), userPage.getTotalElements(), userPage.getTotalPages(), userPage.getNumber() + 1);
    }
    // Implement Owner Service End
}
