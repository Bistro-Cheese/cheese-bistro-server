package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.auth.factory.AuthUserFactory;
import com.ooadprojectserver.restaurantmanagement.auth.model.AuthUser;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserFactory userFactory;
    private final AuthUserFactory authUserFactory;
    @PostMapping
    public ResponseEntity<User> userRegister(@RequestBody UserRegisterRequest request) {
        User user = userFactory.createUser(request);
        AuthUser authUser = authUserFactory.createAuthUser(user);
        logger.info("user la {}", user.getRole());
        logger.info("auth user la {}", authUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
