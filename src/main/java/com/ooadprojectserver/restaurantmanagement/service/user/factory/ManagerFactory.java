package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ManagerFactory extends UserFactory{
    public ManagerFactory(PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        super(passwordEncoder, addressRepository);
    }

    @Override
    protected User createUser(User user, UserCreateRequest userRequest) {
        return new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
    }

    @Override
    protected User updateUser(User user, UserCreateRequest userRequest) {
        return new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
    }
}
