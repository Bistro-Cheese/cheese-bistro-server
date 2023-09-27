package com.ooadprojectserver.restaurantmanagement.controller;


import com.ooadprojectserver.restaurantmanagement.dto.request.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {
    @PostMapping("login")
    public String loginController(@RequestBody UserLoginRequest request) {
        return "Login Successfully";
    }
}
