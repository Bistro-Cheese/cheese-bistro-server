package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.constant.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OwnerService implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final AddressRepository addressRepository;

    @Override
    public User createUser(UserRegisterRequest request) {
        String sDob = request.getDateOfBirth();
        Date dob = null;
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return ownerRepository.save(Owner.ownerBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(dob)
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole().getValue())
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
                .enabled(Objects.equals(request.getStatus(), AccountStatus.ACTIVE_STATUS.getValue()))
                .build());
    }
}
