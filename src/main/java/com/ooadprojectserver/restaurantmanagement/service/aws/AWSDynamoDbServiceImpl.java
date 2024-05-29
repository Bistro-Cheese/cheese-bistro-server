package com.ooadprojectserver.restaurantmanagement.service.aws;

import com.ooadprojectserver.restaurantmanagement.model.aws.UserAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/

@Service
public class AWSDynamoDbServiceImpl implements AWSDynamoDbService{

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbTable<UserAction> userActionTable;

    Logger logger = LoggerFactory.getLogger(AWSDynamoDbServiceImpl.class);

    public AWSDynamoDbServiceImpl(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        userActionTable = dynamoDbEnhancedClient.table("test-bistro-user_action", TableSchema.fromBean(UserAction.class));
    }

    @Override
    public void putUserAction(UserAction item){
        logger.info(item.toString());
        this.userActionTable.putItem(item);
    }
}
