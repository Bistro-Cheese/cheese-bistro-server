package com.ooadprojectserver.restaurantmanagement.config.aws;

import com.ooadprojectserver.restaurantmanagement.aop.TracingControllerAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.profiles.ProfileFileSupplier;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/29/2024, Wednesday
 * @description:
 **/

@Configuration
@EnableConfigurationProperties({AWSCredentialProperty.class, CloudWatchProperty.class})
@Slf4j
public class AWSCredentialConfiguration {

    private final Logger logger = Logger.getLogger(TracingControllerAspect.class.getName());
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
}
