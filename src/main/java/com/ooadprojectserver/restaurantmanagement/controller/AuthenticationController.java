package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.AUTH)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(APIConstant.REGISTER)
    public ResponseEntity<MessageResponse> registerController(@RequestBody UserRegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(CREATED).body(
                new MessageResponse(MessageConstant.REGISTER_SUCCESS)
        );

    }

    @PostMapping(APIConstant.LOGIN)
    public ResponseEntity<APIResponse<AuthenticationResponse>> loginController(@RequestBody UserLoginRequest request) {
       
      AuthenticationResponse authResponse = authenticationService.login(request);

        return ResponseEntity.status(OK)
                .header(HttpHeaders.SET_COOKIE, authResponse.getRefreshTokenCookie().toString())
                .body(
                        new APIResponse<>(
                                MessageConstant.LOGIN_SUCCESS,
                                authResponse
                        )
                );
    }

    @GetMapping(APIConstant.REFRESH_TOKEN)
    public ResponseEntity<APIResponse<AuthenticationResponse>> refreshTokenController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.REFRESH_TOKEN_SUCCESS,
                        authenticationService.refreshToken(request, response)
                )
        );
    }
}
