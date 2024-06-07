package com.ooadprojectserver.restaurantmanagement.service.user.factory;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Role;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Status;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public abstract class UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final UserDetailService userDetailService;

    public User create(UserCreateRequest userRequest) {
        Date dob = getDate(userRequest.getDateOfBirth());

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
                .role(Role.convertIntegerToRole(userRequest.getRole()))
                .status(userRequest.getStatus())
                .enabled(userRequest.getStatus() == Status.ACTIVE.ordinal())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .address(address)
                .dateOfBirth(dob)
                .avatar(userRequest.getAvatar())
                .build();
        user.setCommonCreate(userDetailService.getIdLogin());
        return createUser(user, userRequest);
    }

    public User update(User user, UserCreateRequest userRequest) {
        Date dob = getDate(userRequest.getDateOfBirth());

        Address address = user.getAddress();
        address.setAddressLine(userRequest.getAddressLine());
        address.setCity(userRequest.getCity());
        address.setRegion(userRequest.getRegion());
        addressRepository.save(address);

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setStatus(userRequest.getStatus());
        user.setEnabled(userRequest.getStatus() == Status.ACTIVE.ordinal());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setDateOfBirth(dob);
        user.setAvatar(userRequest.getAvatar());
        user.setAddress(address);

        user.setCommonUpdate(userDetailService.getIdLogin());

        return updateUser(user, userRequest);
    }

    protected abstract User createUser(User user, UserCreateRequest userRequest);

    protected abstract User updateUser(User user, UserCreateRequest userRequest);

    private Date getDate(String dateOfBirth ){
        DateFormat dateFormat = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE);
        try {
            return dateFormat.parse(dateOfBirth);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(APIStatus.WRONG_FORMAT_DATE);
        }
    }
}
