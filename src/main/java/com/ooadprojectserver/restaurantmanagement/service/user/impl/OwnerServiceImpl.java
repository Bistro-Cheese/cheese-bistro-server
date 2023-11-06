package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.OwnerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerFactory ownerFactory;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final UserService[] services;
    private final Map<Integer, UserService> roleToServiceMap;

    public OwnerServiceImpl (
            OwnerFactory ownerFactory,
            UserRepository userRepository,
            OwnerRepository ownerRepository,
            ManagerService managerService,
            StaffService staffService,
            Map<Integer, UserService> roleToServiceMap
    ) {
        this.ownerFactory = ownerFactory;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.managerService = managerService;
        this.staffService = staffService;
        this.services = new UserService[]{this, managerService, staffService};
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
    public void saveUser(UserRegisterRequest userRequest) {
        ownerRepository.save(ownerFactory.create(userRequest));
    }
    // Implement User Service End

    // Implement Owner Service Start
    @Override
    public void createUser(UserRegisterRequest userRequest) {
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(user -> {
            throw new CustomException(APIStatus.EMAIL_ALREADY_EXISTED);
        });
        UserService userService = roleToServiceMap.get(userRequest.getRole());
        if (userService == null) {
            throw new CustomException(APIStatus.INVALID_ROLE_ID);
        } else {
            userService.saveUser(userRequest);
        }
    }
    // Implement Owner Service End


//    public PagingResponseModel searchUser(String name, String role, String sort, int pageNumber, int pageSize) {
//        if (pageSize < 1 || pageNumber < 1) {
//            throw new CustomException(APIStatus.ERR_PAGINATION);
//        }
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//        Page<User> userPage = userRepository.findAll(new UserSpecification(name, role, sort), pageable);
//        List<UserResponse> userResponseList = new ArrayList<>();
//        if (!userPage.isEmpty()) {
//            for (User user : userPage) {
//                userResponseList.add(this.covertUserToUserResponse(user));
//            }
//        }
//        Page<UserResponse> userResponsePage = new PageImpl<>(userResponseList);
//        return new PagingResponseModel(
//                userResponsePage.getContent(),
//                userPage.getTotalElements(),
//                userPage.getTotalPages(),
//                userPage.getNumber() + 1
//        );
//    }
//
//    private UserResponse covertUserToUserResponse(User user) {
//        String sRole = switch (user.getRole()) {
//            case 1 -> RoleConstant.ROLE.STAFF.name().toLowerCase();
//            case 2 -> RoleConstant.ROLE.MANAGER.name().toLowerCase();
//            case 3 -> RoleConstant.ROLE.OWNER.name().toLowerCase();
//            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
//        };
//        String sStatus = switch (user.getStatus()) {
//            case 1 -> Status.ACTIVE.getStatus();
//            case 2 -> Status.INACTIVE.getStatus();
//            default -> throw new IllegalStateException("Unexpected value: " + user.getStatus());
//        };
//        return UserResponse.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .phoneNumber(user.getPhoneNumber())
//                .dateOfBirth(user.getDateOfBirth())
//                .address(user.getAddress())
//                .status(sStatus)
//                .role(sRole)
//                .build();
//    }
}
