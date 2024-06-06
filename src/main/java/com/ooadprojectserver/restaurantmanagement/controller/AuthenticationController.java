package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(APIConstant.AUTH)
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping(APIConstant.LOGIN)
    public ResponseEntity<APIResponse<AuthenticationResponse>> loginController(@RequestBody @Valid UserLoginRequest request) {
        AuthenticationResponse authResponse = authenticationService.login(request);
        log.info(authResponse.toString());
        return ResponseEntity.status(OK).header(HttpHeaders.SET_COOKIE, authResponse.getRefreshTokenCookie().toString()).body(new APIResponse<>(MessageConstant.LOGIN_SUCCESS, authResponse));
    }

    @GetMapping(APIConstant.REFRESH_TOKEN)
    public ResponseEntity<APIResponse<AuthenticationResponse>> refreshTokenController(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(new APIResponse<>(MessageConstant.REFRESH_TOKEN_SUCCESS, authenticationService.refreshToken(request)));
    }
}
