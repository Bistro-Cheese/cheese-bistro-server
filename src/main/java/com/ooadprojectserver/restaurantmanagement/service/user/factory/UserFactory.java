package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Status;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public User create(UserRegisterRequest userRequest) {
        Date date = new Date();
        String dateOfBirth = userRequest.getDateOfBirth();
        DateFormat dateFormat = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE);
        Date dob;
        try {
            dob = dateFormat.parse(dateOfBirth);
        } catch (Exception e) {
            throw new CustomException(APIStatus.INVALID_DATE_OF_BIRTH);
        }
        Address address = Address.builder()
                .addressLine(userRequest.getAddressLine())
                .city(userRequest.getCity())
                .region(userRequest.getRegion())
                .build();
        addressRepository.save(address);
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .role(userRequest.getRole())
                .status(userRequest.getStatus())
                .enabled(userRequest.getStatus() == Status.ACTIVE.ordinal())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .address(address)
                .dateOfBirth(dob)
                .createdDate(date)
                .lastModifiedDate(date)
                .build();
        return createUser(user, userRequest);
    }

    public User update(User user, UserRegisterRequest userRequest) {
        Date date = new Date();
        String dateOfBirth = userRequest.getDateOfBirth();
        DateFormat dateFormat = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE);
        Date dob;
        try {
            dob = dateFormat.parse(dateOfBirth);
        } catch (Exception e) {
            throw new CustomException(APIStatus.INVALID_DATE_OF_BIRTH);
        }
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setRole(userRequest.getRole());
        user.setStatus(userRequest.getStatus());
        user.setEnabled(userRequest.getStatus() == Status.ACTIVE.ordinal());
        user.setPassword(userRequest.getPassword());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setDateOfBirth(dob);
        user.setLastModifiedDate(date);
        return updateUser(user, userRequest);
    }

    protected abstract User createUser(User user, UserRegisterRequest userRequest);

    protected abstract User updateUser(User user, UserRegisterRequest userRequest);
}
