package com.ooadprojectserver.restaurantmanagement.exception.handler;

import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class UploadingFileExceptionHandler {
    Logger logger = LoggerFactory.getLogger(UploadingFileExceptionHandler.class);
    @ExceptionHandler(value = MultipartException.class)
    public MessageResponse handleFileUploadingError(Exception exception) {
        logger.warn("Failed to upload attachment", exception);
        return MessageResponse.builder()
                .message("Failed to upload attachment")
                .build();
    }
}
