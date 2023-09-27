package com.ooadprojectserver.restaurantmanagement.service;

import com.ooadprojectserver.restaurantmanagement.auth.factory.AuthUserFactory;
import com.ooadprojectserver.restaurantmanagement.auth.model.AuthUser;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserFactory userFactory;
    private final AuthUserFactory  authUserFactory;
    private final JwtService jwtService;

    public AuthenticationResponse register(UserRegisterRequest request) {
        User user = userFactory.createUser(request);
        AuthUser authUser = authUserFactory.createAuthUser(user);

        //Generate AT and RT
        String access_token = jwtService.generateAccessToken(authUser);
        String refresh_token = jwtService.generateRefreshToken(authUser);

        return new AuthenticationResponse(
                access_token,
                refresh_token
        );
    }
}
