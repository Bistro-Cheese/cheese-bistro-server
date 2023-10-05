package com.ooadprojectserver.restaurantmanagement.controller;


import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.model.user.factory.UserFactory;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.USERS)
public class UserController {
    private final UserFactory userFactory;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<User>>> getUsersController() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_USERS_SUCCESS,
                        userService.getUsers()
                )
        );
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<MessageResponse> deleteUserController(@PathVariable UUID user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_USER_SUCCESS)
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<User>> getProfileController(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        userService.getProfile(request, response)
                )
        );
    }

    @PatchMapping(APIConstant.PROFILE)
    public ResponseEntity<MessageResponse> updateProfileController(
            @RequestBody UpdateProfileRequest updateRequestBody,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        userService.updateProfile(updateRequestBody, request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_PROFILE_SUCCESS)
        );
    }
}
