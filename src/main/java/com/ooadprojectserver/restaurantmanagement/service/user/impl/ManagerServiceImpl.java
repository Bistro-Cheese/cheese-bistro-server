package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.ManagerService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.ManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerFactory managerFactory;
    private final ManagerRepository managerRepository;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        managerRepository.save(managerFactory.update(user, userRegisterRequest));
    }
    // UserService implementation End
}
