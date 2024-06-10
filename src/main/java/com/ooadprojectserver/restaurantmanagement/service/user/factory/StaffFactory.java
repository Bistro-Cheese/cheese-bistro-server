package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StaffFactory extends UserFactory {
    public StaffFactory(PasswordEncoder passwordEncoder, AddressRepository addressRepository, UserDetailService userDetailService) {
        super(passwordEncoder, addressRepository, userDetailService);
    }

    @Override
    protected User createUser(User user, UserCreateRequest userRequest) {
        Staff newStaff = new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
        newStaff.setCreatedAt(user.getCreatedAt());
        newStaff.setCreatedBy(user.getCreatedBy());
        newStaff.setUpdatedAt(user.getUpdatedAt());
        newStaff.setUpdatedBy(user.getUpdatedBy());
        return newStaff;
    }

    @Override
    protected User updateUser(User user, UserCreateRequest userRequest) {
        User updateStaff = new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
        updateStaff.setId(user.getId());
        updateStaff.setCreatedAt(user.getCreatedAt());
        updateStaff.setCreatedBy(user.getCreatedBy());
        updateStaff.setUpdatedAt(user.getUpdatedAt());
        updateStaff.setUpdatedBy(user.getUpdatedBy());
        return updateStaff;
    }
}
