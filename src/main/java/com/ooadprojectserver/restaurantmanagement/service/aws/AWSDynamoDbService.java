package com.ooadprojectserver.restaurantmanagement.service.aws;

import com.ooadprojectserver.restaurantmanagement.model.aws.UserAction;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/
public interface AWSDynamoDbService {
    public void putUserAction(UserAction object);
}
