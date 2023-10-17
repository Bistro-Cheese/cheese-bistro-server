package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.model.user.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Owner;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final AddressRepository addressRepository;

    public void createUser(UserRegisterRequest request) {
        String sDob = request.getDateOfBirth();
        Date dob;
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ownerRepository.save(Owner.ownerBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(dob)
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(request.getStatus())
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine(request.getAddressLine())
                                        .city(request.getCity())
                                        .region(request.getRegion())
                                        .build()
                        )
                )
                .licenseBusiness(request.getLicenseBusiness())
                .branch(request.getBranch())
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(Objects.equals(request.getStatus(), AccountStatus.ACTIVE.getValue()))
                .build());
    }

    public void updateUser(UUID user_id, UpdateProfileRequest request) {
        ownerRepository.updateOwner(
                request.getBranch(),
                request.getLicenseBusiness(),
                user_id
        );
    }
}
