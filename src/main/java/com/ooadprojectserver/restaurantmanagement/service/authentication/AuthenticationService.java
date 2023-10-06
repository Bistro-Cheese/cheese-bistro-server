package com.ooadprojectserver.restaurantmanagement.service.authentication;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.model.token.Token;
import com.ooadprojectserver.restaurantmanagement.model.token.TokenType;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.TokenRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserFactory userFactory;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Value("${application.security.jwt.refreshCookieName}")
    private String CookieName;
    private final AddressRepository addressRepository;

    public void register(UserRegisterRequest request) {
        User user = userFactory.createUser(request);
    }

    public AuthenticationResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return this.generateToken(user);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String refreshToken;
        Cookie[] cookies = request.getCookies();
        Stream<Cookie> stream = Objects.nonNull(cookies) ? Arrays.stream(cookies) : Stream.empty();
        refreshToken = stream.filter(cookie -> CookieName.equals(cookie.getName())).findFirst().orElse(new Cookie(CookieName, null)).getValue();
        final String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new NoSuchElementException();
        }
        User user = this.userRepository.findByUsername(username).orElseThrow();
        if (jwtService.isTokenValid(refreshToken, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            return AuthenticationResponse.builder().accessToken(accessToken).build();
        }
        return null;
    }


    public User getProfile(
            HttpServletRequest request
    ) {
        String accessToken = getTokenFromHeader(request);
        String username = jwtService.extractUsername(accessToken);
        if (username == null) {
            throw new NoSuchElementException();
        }
        return this.userRepository.findByUsername(username).orElseThrow();
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.contains("Bearer ")) {
            throw new RuntimeException("No Authorization Header");
        }
        return authHeader.substring(7);
    }

    private AuthenticationResponse generateToken(User user) {
        //Generate AT and RT
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        ResponseCookie cookie = jwtService.generateRefreshJwtCookie(refreshToken);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder().accessToken(accessToken).refreshTokenCookie(cookie).build();
    }

    private void saveUserToken(User user, String accessToken) {
        Token token = Token.builder().user(user).token(accessToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();

        tokenRepository.save(token);
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
