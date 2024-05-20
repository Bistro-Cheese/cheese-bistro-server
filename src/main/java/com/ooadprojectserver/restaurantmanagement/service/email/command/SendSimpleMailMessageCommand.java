package com.ooadprojectserver.restaurantmanagement.service.email.command;

import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/20/2024, Monday
 * @description:
 **/

@Getter
@Setter
public class SendSimpleMailMessageCommand extends EmailCommand{

    private EmailRequest emailRequest;

    public SendSimpleMailMessageCommand(EmailService emailService, EmailRequest emailRequest){
        super(emailService);
        this.emailRequest = emailRequest;
    }

    @Override
    public void execute() {
        this.emailService.sendSimpleMailMessage(emailRequest);
    }
}
