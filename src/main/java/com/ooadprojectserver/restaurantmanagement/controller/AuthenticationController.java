package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.AUTH)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(APIConstant.REGISTER)
    public ResponseEntity<APIResponse<AuthenticationResponse>> registerController(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(CREATED).body(
                new APIResponse<>(
                        MessageConstant.REGISTER_SUCCESS,
                        authenticationService.register(request)
                )
        );

    }

    @PostMapping(APIConstant.LOGIN)
    public ResponseEntity<APIResponse<AuthenticationResponse>> loginController(@RequestBody UserLoginRequest request) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.LOGIN_SUCCESS,
                        authenticationService.login(request)
                )
        );
    }

    @PostMapping(APIConstant.REFRESH_TOKEN)
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
