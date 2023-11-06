package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendEmail(
            @RequestBody EmailRequest request
    ) {
        emailService.sendSimpleMailMessage(request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.EMAIL_SEND_SUCCESS)
        );
    }
    @PostMapping("/send-file")
    public ResponseEntity<MessageResponse> sendEmailFile(
            @RequestBody EmailRequest request
    ) {
        emailService.sendMimeMessageWithAttachments(request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.EMAIL_SEND_SUCCESS)
        );
    }
}
