package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.PagingRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import com.ooadprojectserver.restaurantmanagement.service.user.*;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@EnableCaching
@RequestMapping(APIConstant.USERS)
public class UserController {
    private final OwnerService ownerService;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final UserDetailService userDetailService;

    private Map<Integer, UserService> roleToServiceMap;

    @PostConstruct
    private void initRoleToServiceMap() {
        roleToServiceMap = new HashMap<>();
        roleToServiceMap.put(0, ownerService);
        roleToServiceMap.put(1, managerService);
        roleToServiceMap.put(2, staffService);
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createUser(@RequestBody @Valid UserCreateRequest userRegisterRequest) {
        ownerService.createUser(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_USER_SUCCESS));
    }

    @GetMapping()
    @Cacheable(value = "users")
    public ResponseEntity<APIResponse<List<UserResponse>>> getUsers() {
        Integer role = userDetailService.getRoleLogin();
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_USERS_SUCCESS,
                        switch (role) {
                            case 0 -> ownerService.getUsers();
                            case 1 -> managerService.getUsers();
                            default -> throw new IllegalStateException("Unexpected value: " + role);
                        }
                )
        );
    }

    @GetMapping(APIConstant.USER_ID)
    @Cacheable(value = "user", key = "#userId")
    public ResponseEntity<APIResponse<User>> getUserDetail(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_USER_DETAIL_SUCCESS,
                        ownerService.getUserDetail(userId)
                )
        );
    }

    @GetMapping(APIConstant.PROFILE)
    public ResponseEntity<APIResponse<UserResponse>> getProfile() {
        Integer role = userDetailService.getRoleLogin();
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        MessageConstant.GET_PROFILE_SUCCESS,
                        roleToServiceMap.get(role).getProfile()
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
    public ResponseEntity<MessageResponse> updateUser(@PathVariable UUID userId, @RequestBody UserCreateRequest userCreateRequest) {
        ownerService.updateUser(userId, userCreateRequest);
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
