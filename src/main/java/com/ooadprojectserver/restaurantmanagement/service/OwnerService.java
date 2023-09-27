package com.ooadprojectserver.restaurantmanagement.service;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final AuthenticationService authenticationService;
    public AuthenticationResponse register(UserRegisterRequest request) {
        return authenticationService.register(request);
    }
}
