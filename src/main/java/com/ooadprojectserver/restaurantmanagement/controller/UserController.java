package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.PagingRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.service.user.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final OwnerService ownerService;
    @PostMapping()
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        ownerService.createUser(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_USER_SUCCESS));
    }

    @GetMapping()
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_USERS_SUCCESS,
                        ownerService.getAllUsers()
                )
        );
    }

    @GetMapping(APIConstant.USER_ID)
    public ResponseEntity<APIResponse<UserResponse>> getUserDetail(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_USER_DETAIL_SUCCESS,
                        ownerService.getUserDetail(userId)
                )
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<UserResponse>> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        ownerService.getProfile(request)
                )
        );
    }

    @DeleteMapping(APIConstant.USER_ID)
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable UUID userId) {
        ownerService.deleteUser(userId);
        return ResponseEntity.ok().body(
                new MessageResponse(MessageConstant.DELETE_USER_SUCCESS)
        );
    }

    @PutMapping(APIConstant.USER_ID)
    public ResponseEntity<MessageResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRegisterRequest userRegisterRequest) {
        ownerService.updateUser(userId, userRegisterRequest);
        return ResponseEntity.ok().body(
                new MessageResponse(MessageConstant.UPDATE_USER_SUCCESS)
        );
    }

    @GetMapping(APIConstant.SEARCH)
    public ResponseEntity<APIResponse<PagingResponse>> searchUser(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String role,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "1", value = "page_number") int pageNumber,
            @RequestParam(defaultValue = "10", value = "page_size") int pageSize
    ) {
        SearchRequest searchRequest = SearchRequest.builder()
                .params(List.of(name, role, sort))
                .pagingRequest(new PagingRequest(pageNumber, pageSize))
                .build();
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.SEARCH_USER_SUCCESS,
                        ownerService.searchUser(searchRequest)
                )
        );
    }
}
