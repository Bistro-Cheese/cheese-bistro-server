package com.ooadprojectserver.restaurantmanagement.exception;

import com.ooadprojectserver.restaurantmanagement.util.DataUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class AppException extends ServiceException {
    String code;
    String message;
    List<String> errorField;
    String type;
    Boolean handleException = true;

    public AppException(String error, String message) {
        super(message);
        this.message = message;
        this.code = error;
    }

    public AppException(String error, String message, List<String> errorField) {
        super(message);
        this.message = message;
        this.code = error;
        this.errorField = errorField;
    }
    
    public AppException(String error, List<Object> errorField) {
        super(error);
        this.code = error;
        this.errorField = errorField.stream().map(DataUtil::safeToString).collect(Collectors.toList());
    }

    public AppException(String error) {
        super("");
        this.code = error;
    }
}
