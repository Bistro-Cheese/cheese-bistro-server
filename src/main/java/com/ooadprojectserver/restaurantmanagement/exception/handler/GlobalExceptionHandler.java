package com.ooadprojectserver.restaurantmanagement.exception.handler;

import com.ooadprojectserver.restaurantmanagement.dto.response.model.ErrorResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getApiStatus().getMessage(),
                exception.getApiStatus().name(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(exception.getApiStatus().getStatus()).body(errorResponse);
    }
}
