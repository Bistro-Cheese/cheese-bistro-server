package com.ooadprojectserver.restaurantmanagement.service.factory.implement;


import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final ManagerRepository managerRepository;
    private final OwnerRepository ownerRepository;
    @Override
    public User createUser(UserRegisterRequest user) {
        return switch (user.getRole()) {
            case STAFF ->{
                Staff staff = Staff.staffBuilder()
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .hashPassword(passwordEncoder.encode(user.getPassword()))
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole().getValue())
                        .status(user.getStatus())
                        .createAt(LocalDateTime.now())
                        .academicLevel(user.getAcademicLevel())
                        .foreignLanguage(user.getForeignLanguage())
                        .build();
                yield staffRepository.save(staff);
            }
            case MANAGER -> {
                Manager manager = Manager.managerBuilder()
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .hashPassword(passwordEncoder.encode(user.getPassword()))
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole().getValue())
                        .status(user.getStatus())
                        .createAt(LocalDateTime.now())
                        .foreignLanguage(user.getForeignLanguage())
                        .experiencedYear(user.getExperiencedYear())
                        .certificationManagement(user.getCertificationManagement())
                        .build();
                yield managerRepository.save(manager);
            }
            case OWNER ->{
                Owner owner = Owner.ownerBuilder().username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .hashPassword(passwordEncoder.encode(user.getPassword()))
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole().getValue())
                        .status(user.getStatus())
                        .licenseBusiness(user.getLicenseBusiness())
                        .branch(user.getBranch())
                        .createAt(LocalDateTime.now()).build();
                yield ownerRepository.save(owner);
            }
            default -> throw new RuntimeException();
        };
    }
}
