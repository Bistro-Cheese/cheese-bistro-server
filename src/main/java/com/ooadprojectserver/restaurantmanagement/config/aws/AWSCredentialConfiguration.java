package com.ooadprojectserver.restaurantmanagement.config.aws;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/

@Configuration
@EnableConfigurationProperties({AWSCredentialProperty.class, CloudWatchProperty.class})
public class AWSCredentialConfiguration {

    private final AWSCredentialProperty awsCredentialProperty;

    public AWSCredentialConfiguration(AWSCredentialProperty awsCredentialProperty) {
        this.awsCredentialProperty = awsCredentialProperty;
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbClient() {
        DynamoDbAsyncClient dynamoDbAsyncClient = DynamoDbAsyncClient.create();

        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }

    @Bean
    public SqsAsyncClient sqsClient(){
        return SqsAsyncClient.create();
    }
}
