package com.ooadprojectserver.restaurantmanagement.service.user.impl;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.factory.StaffFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffFactory staffFactory;
    private final StaffRepository staffRepository;

    // UserService implementation Start
    @Override
    public void saveUser(UserRegisterRequest userRegisterRequest) {
        staffRepository.save(staffFactory.create(userRegisterRequest));
    }

    @Override
    public void updateUserById(User user, UserRegisterRequest userRegisterRequest) {
        staffRepository.save(staffFactory.update(user, userRegisterRequest));
    }
    // UserService implementation End
}

