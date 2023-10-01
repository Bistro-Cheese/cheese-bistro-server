package com.ooadprojectserver.restaurantmanagement.service.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.model.token.Token;
import com.ooadprojectserver.restaurantmanagement.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.contains("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            try {
                response.setContentType("application/json");
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        APIResponse.builder()
                                .message(MessageConstant.LOGOUT_SUCCESS)
                                .build()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new NoSuchElementException("Token Not Found");
        }
    }
}
