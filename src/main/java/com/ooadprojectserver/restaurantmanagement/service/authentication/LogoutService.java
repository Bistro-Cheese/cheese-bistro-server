package com.ooadprojectserver.restaurantmanagement.service.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.token.Token;
import com.ooadprojectserver.restaurantmanagement.model.user.type.User;
import com.ooadprojectserver.restaurantmanagement.repository.user.TokenRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.contains("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        User user = userRepository.findByUsername(username).orElseThrow();
        UUID user_id = user.getId();

        List<Token> tokens = tokenRepository.findAllTokenByUserId(user_id);
        tokenRepository.deleteAll(tokens);
        response.setContentType("application/json");
        try {
            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new MessageResponse(MessageConstant.LOGOUT_SUCCESS)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
