package com.ooadprojectserver.restaurantmanagement.exception;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import lombok.*;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final APIStatus apiStatus;
}
