package com.ooadprojectserver.restaurantmanagement.service.authentication;

import com.ooadprojectserver.restaurantmanagement.auth.factory.AuthUserFactory;
import com.ooadprojectserver.restaurantmanagement.auth.model.AuthUser;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserFactory userFactory;
    private final AuthUserFactory authUserFactory;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponse generateToken(AuthUser authUser) {
        //Generate AT and RT
        String access_token = jwtService.generateAccessToken(authUser);
        String refresh_token = jwtService.generateRefreshToken(authUser);

        return new AuthenticationResponse(
                access_token,
                refresh_token
        );
    }

    public AuthenticationResponse register(UserRegisterRequest request) {
        User user = userFactory.createUser(request);
        AuthUser authUser = authUserFactory.createAuthUser(user);
        return this.generateToken(authUser);
    }

    public AuthenticationResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        AuthUser authUser = authUserFactory.createAuthUser(user);
        return this.generateToken(authUser);
    }
}
