package com.ooadprojectserver.restaurantmanagement.exception;

import com.ooadprojectserver.restaurantmanagement.constant.ParamsError;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

// Unchecked exception
@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {
    private APIStatus apiStatus;
    private List<ParamsError> data;

    /*
    * This constructor is built only for handling BAD REQUEST exception
    * Careful when use it with other purpose ;)
    */

    public ApplicationException(APIStatus cause) {
        super(String.valueOf(cause));
    }
}
