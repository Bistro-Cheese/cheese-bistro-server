package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.USERS)
public class UserController {
    private final UserService userService;

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

    @GetMapping(APIConstant.SEARCH)
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
