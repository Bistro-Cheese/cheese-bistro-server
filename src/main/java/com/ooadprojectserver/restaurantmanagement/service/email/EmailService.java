package com.ooadprojectserver.restaurantmanagement.service.email;

import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;

import java.util.Locale;

public interface EmailService {
    void sendSimpleMailMessage(EmailRequest request);
    void sendMimeMessageWithAttachments(EmailRequest request);

    void sendMailWithInline(ConfirmationRequest request, Locale locale);
}
