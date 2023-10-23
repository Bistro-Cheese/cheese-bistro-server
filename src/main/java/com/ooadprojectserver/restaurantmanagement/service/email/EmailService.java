package com.ooadprojectserver.restaurantmanagement.service.email;

import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;

public interface EmailService {
    void sendSimpleMailMessage(EmailRequest request);
    void sendMimeMessageWithAttachments(EmailRequest request);

//    void sendMimeMessageWithEmbeddedFiles(EmailRequest request, String filePath);
}
