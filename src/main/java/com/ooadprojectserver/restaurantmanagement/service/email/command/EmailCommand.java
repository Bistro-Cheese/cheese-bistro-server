package com.ooadprojectserver.restaurantmanagement.service.email.command;

import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/20/2024, Monday
 * @description:
 **/
public abstract class EmailCommand {
    protected final EmailService emailService;

    public EmailCommand(EmailService emailService) {
        this.emailService = emailService;
    }

    public abstract void execute();
}
