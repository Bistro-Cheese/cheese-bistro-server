package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.AuthenticationResponse;
import com.ooadprojectserver.restaurantmanagement.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/")
public class OwnerController {
    private final OwnerService ownerService;
    @PostMapping("register")
    public APIResponse<AuthenticationResponse> registerController(@RequestBody UserRegisterRequest request) {
        return new APIResponse<AuthenticationResponse>(
                            201,
                            "Owner Register Successfully",
                            ownerService.register(request)
        );
    }

    @PostMapping("add-manager")
    public ResponseEntity<String> addUser(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok("Add User Successfully");
    }
}
