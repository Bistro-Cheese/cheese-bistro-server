package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OwnerFactory extends UserFactory {
    public OwnerFactory(PasswordEncoder passwordEncoder, AddressRepository addressRepository, UserDetailService userDetailService) {
        super(passwordEncoder, addressRepository, userDetailService);
    }

    @Override
    protected User createUser(User user, UserCreateRequest userRequest) {
        return new Owner(
                user,
                userRequest.getBranch(),
                userRequest.getLicenseBusiness()
        );
    }

    @Override
    protected User updateUser(User user, UserCreateRequest userRequest) {
        return new Owner(
                user,
                userRequest.getBranch(),
                userRequest.getLicenseBusiness()
        );
    }
}
