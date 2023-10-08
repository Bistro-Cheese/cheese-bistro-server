package com.ooadprojectserver.restaurantmanagement.exception;

import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import lombok.*;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final APIStatus apiStatus;
}
