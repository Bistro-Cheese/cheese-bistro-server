package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StaffFactory extends UserFactory {
    public StaffFactory(PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        super(passwordEncoder, addressRepository);
    }

    @Override
    protected User createUser(User user, UserCreateRequest userRequest) {
        return new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
    }

    @Override
    protected User updateUser(User user, UserCreateRequest userRequest) {
        return new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
    }
}
