package com.ooadprojectserver.restaurantmanagement.dto.response;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PagedResponse;
import lombok.*;
import org.springframework.data.domain.Page;

@Data
@Builder
public class APIResponse<T> {
    private String code;
    private String message;
    private T data;

    public APIResponse(APIStatus apiStatus, T data) {

        if (apiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.message = apiStatus.getMessage();
        this.data = data;
    }

    public APIResponse() {
        super();
        this.code = APIStatus.SUCCESS_CODE.getMessage() ;
        this.message = "SUCCESS";
    }

    public APIResponse(String code, String message, T body) {
        super();
        this.code = code;
        this.message = message;
        this.data = body;
    }

    public APIResponse(T body) {
        super();
        this.code = APIStatus.SUCCESS_CODE.getMessage();
        this.message = "SUCCESS";
        this.data = body;
    }

    public APIResponse(Page body) {
        super();
        this.code = APIStatus.SUCCESS_CODE.getMessage();
        this.message = "SUCCESS";
        this.data = (T) PagedResponse.builder().page(body).build();
    }

    public APIResponse(String message, T body) {
        this.message = message;
        this.data = body;
    }

    public APIResponse<T> emptyBody() {
        return this;
    }

    protected APIResponse<T> withCode(String code) {
        this.code = code;
        return this;
    }

    public APIResponse<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public APIResponse<T> withBody(T body) {
        this.data = body;
        return this;
    }

}
