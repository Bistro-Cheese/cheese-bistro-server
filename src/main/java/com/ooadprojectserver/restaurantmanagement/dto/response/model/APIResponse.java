package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import lombok.*;

import java.io.Serializable;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class APIResponse<T> implements Serializable {
    private int status;
    private String message;
    private T data;

    public APIResponse(APIStatus apiStatus, T data) {

        if (apiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = apiStatus.getStatus();
        this.message = apiStatus.getMessage();
        this.data = data;
    }

}
