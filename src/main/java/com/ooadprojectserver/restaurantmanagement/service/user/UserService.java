package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.repository.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthenticationService authenticationService;
    private final UserFactory userFactory;
    private final JwtService jwtService;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID user_id) {
        this.userRepository.deleteById(user_id);
    }

    public User getProfile(HttpServletRequest request) {
        String accessToken = authenticationService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(accessToken);
        if (username == null) {
            throw new NoSuchElementException();
        }
        return this.userRepository.findByUsername(username).orElseThrow();
    }

    public void updateProfile(UpdateProfileRequest updateRequestBody, HttpServletRequest request) {
        String accessToken = authenticationService.getTokenFromHeader(request);
        String username = jwtService.extractUsername(accessToken);
        if (username == null) {
            throw new NoSuchElementException();
        } else {
            User user = this.userRepository.findByUsername(username).orElseThrow();
            Date lastModifiedDate = new Date();

            //  Covert String to Date
            String sDob = updateRequestBody.getDateOfBirth();
            Date dob = new Date();
            try {
                dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            //  Update Address
            addressRepository.updateAddressById(
                    updateRequestBody.getAddressLine(),
                    updateRequestBody.getCity(),
                    updateRequestBody.getRegion(),
                    user.getAddress().getId()
            );

            // Update User
            userRepository.updateUserById(
                    updateRequestBody.getFirstName(),
                    updateRequestBody.getLastName(),
                    lastModifiedDate,
                    dob,
                    updateRequestBody.getPhoneNumber(),
                    user.getId()
            );

            userFactory.updateUser(user, updateRequestBody);
        }
    }
}
