package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class APIResponse<T> implements Serializable {
    private String message;
    private T data;

    public APIResponse(APIStatus apiStatus, T data) {

        if (apiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.message = apiStatus.getMessage();
        this.data = data;
    }

}
