package com.ooadprojectserver.restaurantmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class StatusResponse<T> {
    private T result;
    private String description;
    private int statusCode;
    private Long totalRecords;
    private Long serverTime;

    public StatusResponse() {
    }

    public StatusResponse(APIStatus status) {
        this.statusCode = status.getStatus();
        this.description = status.getMessage();
    }

    public StatusResponse(APIStatus status, T result) {
        this.statusCode = status.getStatus();
        this.description = status.getMessage();
        this.result = result;
    }

    public StatusResponse(int status, T result) {
        this.statusCode = status;
        this.result = result;
    }

    public StatusResponse(int status, T result, long totalRecords) {
        this.statusCode = status;
        this.result = result;
        this.totalRecords = totalRecords;
    }

    public StatusResponse(T result, String description, int statusCode) {
        this.result = result;
        this.description = description;
        this.statusCode = statusCode;
    }

    public StatusResponse(int status, String desc) {
        this.statusCode = status;
        this.description = desc;
    }

    public void setStatusCode(int status) {
        this.statusCode = status;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
}
