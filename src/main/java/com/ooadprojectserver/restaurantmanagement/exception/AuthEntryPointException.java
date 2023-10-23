package com.ooadprojectserver.restaurantmanagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ooadprojectserver.restaurantmanagement.dto.response.StatusResponse;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class AuthEntryPointException implements AuthenticationEntryPoint, Serializable {
    @Serial
    private static final long serialVersionUID = -8970718410437077606L;
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new StatusResponse<>(APIStatus.ERR_UNAUTHORIZED)));
    }
}
