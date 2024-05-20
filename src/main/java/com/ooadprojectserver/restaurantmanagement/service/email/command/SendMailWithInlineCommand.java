package com.ooadprojectserver.restaurantmanagement.service.email.command;

import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/20/2024, Monday
 * @description:
 **/

@Getter
@Setter
public class SendMailWithInlineCommand extends EmailCommand{
    private final Locale locale;
    private final ConfirmationRequest confirmationRequest;
    public SendMailWithInlineCommand(EmailService emailService, ConfirmationRequest confirmationRequest, Locale locale){
        super(emailService);
        this.locale = locale;
        this.confirmationRequest = confirmationRequest;
    }
    @Override
    public void execute() {
        this.emailService.sendMailWithInline(confirmationRequest, locale);
    }
}
