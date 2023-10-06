package com.ooadprojectserver.restaurantmanagement.controller;


import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.USERS)
public class UserController {
    private final UserFactory userFactory;
    private final AuthenticationService authenticationService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<User>>> getUsersController() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_USERS,
                        userFactory.getUsers()
                )
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<User>> getMeController(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        authenticationService.getProfile(request)
                )
        );
    }
}
