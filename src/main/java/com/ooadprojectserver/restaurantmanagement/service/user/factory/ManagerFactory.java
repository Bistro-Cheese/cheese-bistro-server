package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ManagerFactory extends UserFactory{
    public ManagerFactory(PasswordEncoder passwordEncoder, AddressRepository addressRepository, UserDetailService userDetailService) {
        super(passwordEncoder, addressRepository, userDetailService);
    }

    @Override
    protected Manager createUser(User user, UserCreateRequest userRequest) {
        Manager newManager = new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
        newManager.setCreatedAt(user.getCreatedAt());
        newManager.setCreatedBy(user.getCreatedBy());
        newManager.setUpdatedAt(user.getUpdatedAt());
        newManager.setUpdatedBy(user.getUpdatedBy());
        return newManager;
    }

    @Override
    protected Manager updateUser(User user, UserCreateRequest userRequest) {
        Manager updateManager = new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
        updateManager.setId(user.getId());
        updateManager.setCreatedAt(user.getCreatedAt());
        updateManager.setCreatedBy(user.getCreatedBy());
        updateManager.setUpdatedAt(user.getUpdatedAt());
        updateManager.setUpdatedBy(user.getUpdatedBy());
        return updateManager;
    }
}
