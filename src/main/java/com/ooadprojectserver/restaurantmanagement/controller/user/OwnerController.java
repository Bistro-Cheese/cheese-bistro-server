package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.OWNER)
@PreAuthorize("hasRole('OWNER')")
public class OwnerController {
    private final EmailService emailService;

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
