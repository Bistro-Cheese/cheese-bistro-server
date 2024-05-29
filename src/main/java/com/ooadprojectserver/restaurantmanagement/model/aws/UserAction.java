package com.ooadprojectserver.restaurantmanagement.model.aws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class UserAction {
    private String id;
    private String userId;
    private String userName;
    private String methodName;
    private String ipAddress;

    @DynamoDbPartitionKey
    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    @DynamoDbSortKey
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMethodName() {
        return methodName;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
