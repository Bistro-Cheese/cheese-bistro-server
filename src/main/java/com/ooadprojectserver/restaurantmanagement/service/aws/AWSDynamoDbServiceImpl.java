package com.ooadprojectserver.restaurantmanagement.service.aws;

import com.ooadprojectserver.restaurantmanagement.model.aws.UserAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/

@Service
public class AWSDynamoDbServiceImpl implements AWSDynamoDbService{

    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncTable<UserAction> userActionTable;

    Logger logger = LoggerFactory.getLogger(AWSDynamoDbServiceImpl.class);

    public AWSDynamoDbServiceImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
        userActionTable = dynamoDbEnhancedAsyncClient.table("test-bistro-user_action", TableSchema.fromBean(UserAction.class));
    }

    @Override
    public void putUserAction(UserAction item){
        logger.info(item.toString());
        this.userActionTable.putItem(item);
    }
}
