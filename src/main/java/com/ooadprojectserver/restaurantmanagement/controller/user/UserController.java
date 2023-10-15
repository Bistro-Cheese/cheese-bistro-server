package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.UserResponse;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_USERS_SUCCESS,
                        userService.getUsers()
                )
        );
    }

    @PatchMapping(APIConstant.USER_ID)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<MessageResponse> updateUserById(
            @RequestBody UpdateProfileRequest updateRequestBody,
            @PathVariable UUID user_id
    ) {
        userService.updateUser(updateRequestBody, user_id);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.UPDATE_USER_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.USER_ID)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable UUID user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.DELETE_USER_SUCCESS)
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<UserResponse>> getProfile(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        userService.getProfile(request)
                )
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<APIResponse<PagingResponseModel>> searchUser (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String sort,
            @RequestParam(name="page_number", defaultValue = "1") Integer pageNumber,
            @RequestParam(name="page_size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.SEARCH_USER_SUCCESS,
                        userService.searchUser(name, role, sort, pageNumber, pageSize)
                )
        );
    }
}
