package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.USERS)
public class UserController {
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

    @DeleteMapping(APIConstant.USER_ID)
    public ResponseEntity<MessageResponse> deleteUserController(@PathVariable UUID user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_USER_SUCCESS)
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<User>> getMeController(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        userService.getProfile(request)
                )
        );
    }

    @PatchMapping(APIConstant.PROFILE)
    public ResponseEntity<MessageResponse> updateProfileController(
            @RequestBody UpdateProfileRequest updateRequestBody,
            HttpServletRequest request
    ) {
        userService.updateProfile(updateRequestBody, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_PROFILE_SUCCESS)
        );
    }
}
