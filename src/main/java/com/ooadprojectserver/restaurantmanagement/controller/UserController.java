package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final OwnerService ownerService;
    @PostMapping()
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        ownerService.createUser(userRegisterRequest);
        return ResponseEntity.ok(new MessageResponse(MessageConstant.CREATE_USER_SUCCESS));
    }

    @GetMapping()
    public void getAllUsers() {

    }

    @GetMapping("/{userId}")
    public void getUserDetail(@PathVariable String userId) {

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {

    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody UserRegisterRequest userRegisterRequest) {

    }
}
