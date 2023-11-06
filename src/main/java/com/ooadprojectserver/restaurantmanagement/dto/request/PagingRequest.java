package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequest {
    private Integer pageNumber;
    private Integer pageSize;

    public void checkValidPaging() {
        if (pageNumber < 1 || pageSize < 1) {
            throw new CustomException(APIStatus.ERR_PAGINATION);
        }
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(pageNumber - 1, pageSize);
    }
}
