package com.ooadprojectserver.restaurantmanagement.service.aws.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.concurrent.ExecutionException;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/1/2024, Saturday
 * @description:
 **/

@Service
public class SendEmailSqs {

    @Value("${spring.aws.queueUrl}")
    private String queueUrl;
    Logger logger = LoggerFactory.getLogger(SendEmailSqs.class);


    private final SqsAsyncClient sqsClient;
    private final ObjectMapper objectMapper;

    public SendEmailSqs(SqsAsyncClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    public void publishMessage(Object payload) {
        try {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(payload.toString())
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
        } catch (SqsException e) {
            logger.error(e.getMessage());
        }
    }
}
