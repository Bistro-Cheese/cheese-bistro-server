package com.ooadprojectserver.restaurantmanagement.service.email;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.controller.LoggerController;
import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.EmailRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.ooadprojectserver.restaurantmanagement.constant.EmailConstant.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMailMessage(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject(request.getSubject());
            message.setTo(request.getEmailTo());
            message.setText(request.getContent());
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(EmailRequest request) {
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
            }
            {
                throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }
    }

    @Override
    @Async
    public void sendMailWithInline(ConfirmationRequest request, Locale locale) {
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("userFullName", request.getFullName());
        ctx.setVariable("username", request.getUsername());
        ctx.setVariable("password", request.getPassword());

        MimeMessage mimeMessage;
        // Prepare message using a Spring helper
        try {
            mimeMessage = getMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING); // true = multipart
            message.setSubject("Example HTML email with inline image");
            message.setFrom(fromEmail);
            message.setTo(request.getEmailTo());
            // Create the HTML body using Thymeleaf
            final String htmlContent = this.templateEngine.process("html/email-confirmation.html", ctx);
            message.setText(htmlContent, true); // true = isHtml
        } catch (Exception e) {
            log.error("Send email fail");
            log.error(e.getMessage());
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }

        try{
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES,
                    new ArrayBlockingQueue<>(2));

            CompletableFuture<Void> emailTask = CompletableFuture.runAsync(() -> {
                log.info("Send email successfully");
                emailSender.send(mimeMessage);

            });
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }


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
//            log.info("Added a file attachment: {}", fileName);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
//        }
//    }
}
