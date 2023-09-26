package com.ooadprojectserver.restaurantmanagement.service.factory.implement;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.service.implement.ManagerServiceImpl;
import com.ooadprojectserver.restaurantmanagement.service.implement.OwnerServiceImpl;
import com.ooadprojectserver.restaurantmanagement.service.implement.StaffServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
