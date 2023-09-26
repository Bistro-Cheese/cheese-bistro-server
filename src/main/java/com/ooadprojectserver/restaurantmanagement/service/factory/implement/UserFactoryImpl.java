package com.ooadprojectserver.restaurantmanagement.service.factory.implement;


import com.ooadprojectserver.restaurantmanagement.controller.UserController;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.UserService;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.service.implement.ManagerServiceImpl;
import com.ooadprojectserver.restaurantmanagement.service.implement.OwnerServiceImpl;
import com.ooadprojectserver.restaurantmanagement.service.implement.StaffServiceImpl;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {

    Logger logger = LoggerFactory.getLogger(UserFactoryImpl.class);

    private final ManagerServiceImpl managerService;
    private final StaffServiceImpl staffService;
    private final OwnerServiceImpl ownerService;

//    Map<Integer, Class<? extends UserService>> classes = new HashMap<>();
//
//    @PostConstruct
//    private void registerUserClass(){
//        classes.put(Constant.ROLE.STAFF.getValue(), StaffServiceImpl.class);
//        logger.info("registerUserClass is created");
//    }


//    @Override
//    public User createUser(UserRegisterRequest user) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        return classes.get(user.getRole().getValue()).getDeclaredConstructor().newInstance().createUser(user);
//    };

    @Override
    public User createUser(UserRegisterRequest user) {
        return switch (user.getRole()) {
            case STAFF -> staffService.createUser(user);
            case MANAGER -> managerService.createUser(user);
            case OWNER -> ownerService.createUser(user);
        };
    }
}
