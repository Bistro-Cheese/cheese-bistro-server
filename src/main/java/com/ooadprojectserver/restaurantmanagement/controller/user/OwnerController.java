package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.UserResponse;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserService;
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
@RequestMapping(APIConstant.OWNER)
@PreAuthorize("hasRole('OWNER')")
public class OwnerController {
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping(APIConstant.ALL_USERS)
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_USERS_SUCCESS,
                        userService.getUsers()
                )
        );
    }

    @DeleteMapping(APIConstant.OWNER_USER_ID)
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable UUID user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.DELETE_USER_SUCCESS)
        );
    }

    @PutMapping(APIConstant.OWNER_USER_ID)
    public ResponseEntity<MessageResponse> updateUserById(
            @RequestBody UpdateProfileRequest updateRequestBody,
            @PathVariable UUID user_id
    ) throws ParseException {
        userService.updateUser(updateRequestBody, user_id);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.UPDATE_USER_SUCCESS)
        );
    }

    @PostMapping(APIConstant.EMAIL_SEND)
    public ResponseEntity<MessageResponse> sendEmail(
            @RequestBody EmailRequest request
    ) {
        emailService.sendSimpleMailMessage(request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.EMAIL_SEND_SUCCESS)
        );
    }
    @PostMapping(APIConstant.EMAIL_SEND_FILE)
    public ResponseEntity<MessageResponse> sendEmailFile(
            @RequestBody EmailRequest request
    ) {
        emailService.sendMimeMessageWithAttachments(request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.EMAIL_SEND_SUCCESS)
        );
    }
}
