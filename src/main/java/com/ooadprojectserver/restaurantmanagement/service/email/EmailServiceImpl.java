package com.ooadprojectserver.restaurantmanagement.service.email;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    public static final String UTF_8_ENCODING = "UTF-8";
//    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    @Value("${spring.mail.username}")
    private String fromEmail;

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender emailSender;

    @Override
    @Async
    public void sendSimpleMailMessage(EmailRequest request) {
        try{
            logger.info("SENDING MAIL");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject(request.getSubject());
            message.setTo(request.getEmailTo());
            message.setText(request.getContent());
            emailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(EmailRequest request){
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(request.getSubject());
            helper.setFrom(fromEmail);
            helper.setTo(request.getEmailTo());
            helper.setText(request.getContent());
            //Add attachments
            FileSystemResource file
                    = new FileSystemResource(new File(request.getFilePath()));
            if (file.getFilename() != null) {
                helper.addAttachment(file.getFilename(), file);
                emailSender.send(message);
            }{
                throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }
    }

//    @Override
//    public void sendMimeMessageWithEmbeddedFiles(String emailTo, String subject, String content, String multiPaths) {
//
//    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

//    private void addAttachments(MailMessageDto message, MimeMessageHelper helper) {
//        message.getFiles().forEach(file -> addAttachment(file, helper));
//    }
//
//    private void addAttachment(File file, MimeMessageHelper helper) {
//        String fileName = file.getName();
//        try {
//            helper.addAttachment(fileName, file);
//            logger.info("Added a file attachment: {}", fileName);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
//        }
//    }
}
