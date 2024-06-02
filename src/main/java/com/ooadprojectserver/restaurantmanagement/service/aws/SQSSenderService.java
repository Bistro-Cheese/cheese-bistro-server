package com.ooadprojectserver.restaurantmanagement.service.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/1/2024, Saturday
 * @description:
 **/

@Service
public class SQSSenderService {

    @Value("${spring.aws.queueName}")
    private String queueName;
    Logger logger = LoggerFactory.getLogger(SQSSenderService.class);


    private final SqsAsyncClient sqsAsyncClient;

    public SQSSenderService(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public void publishMessage(Object payload) {
        try {
            CompletableFuture<GetQueueUrlResponse> queueUrl = sqsAsyncClient.getQueueUrl(GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build());

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl.get().queueUrl())
                    .messageBody(payload.toString())
                    .build();

            Mono.fromFuture(() -> sqsAsyncClient.sendMessage(
                    sendMsgRequest
            ))
                    .retryWhen(Retry.max(3))
                    .repeat(1)
                    .subscribe();
        } catch (SqsException | ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
