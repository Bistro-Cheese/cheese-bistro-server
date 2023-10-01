package com.ooadprojectserver.restaurantmanagement.service.authentication;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.model.token.Token;
import com.ooadprojectserver.restaurantmanagement.model.token.TokenType;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.TokenRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserFactory userFactory;

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    public AuthenticationResponse generateToken(User user) {
        //Generate AT and RT
        String access_token = jwtService.generateAccessToken(user);
        String refresh_token = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        Token token = Token.builder()
                .user(user)
                .token(access_token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
    }

    public AuthenticationResponse register(UserRegisterRequest request) {
        User user = userFactory.createUser(request);
        return this.generateToken(user);
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
        return this.generateToken(user);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
