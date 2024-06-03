package com.ooadprojectserver.restaurantmanagement.service.aws;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.ConfirmationRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/4/2024, Tuesday
 * @description:
 **/

@Component
@RequiredArgsConstructor
public class SQSEmailSendingListener implements DisposableBean {
    private volatile boolean doPolling;

    @Value("${spring.aws.queueName}")
    private String queueName;

    private final SqsAsyncClient sqsAsyncClient;
    private final EmailService emailService;


    Logger logger = LoggerFactory.getLogger(SQSEmailSendingListener.class);



    ArrayBlockingQueue<Message> messageHoldingQueue = new ArrayBlockingQueue<Message>(
            1);

    @Override
    public void destroy() throws Exception {
        this.doPolling = false;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void run() {
        this.doPolling = true;
        while (doPolling) {
            CompletableFuture<GetQueueUrlResponse> queueUrl = sqsAsyncClient.getQueueUrl(GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build());
            try{
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl.get().queueUrl())
                        .maxNumberOfMessages(1)
                        .visibilityTimeout(15)
                        .waitTimeSeconds(10).build();

                ReceiveMessageResponse receiveMessageResponse = sqsAsyncClient.receiveMessage(receiveMessageRequest).get();

                if (receiveMessageResponse.messages() == null || receiveMessageResponse.messages().isEmpty())
                    continue;
                else {
                    for (Message message : receiveMessageResponse.messages()) {
                        messageHoldingQueue.put(message);
                    }

                    try {
                        Message emailEvent = messageHoldingQueue.poll();
                        //Your specific processing code
                        emailService.sendMailWithInline(extractEmailInfo(emailEvent.body()), Locale.ENGLISH);
                    } catch (Exception e) {
                        logger.error("Exception while processing message from SQS Queue ", e);
                    }
                }
            }catch (ExecutionException | InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private ConfirmationRequest extractEmailInfo(String body){
        Pattern pattern = Pattern.compile("ConfirmationRequest\\((.*?)\\)");
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            String[] params = matcher.group(1).split(", ");
            String emailTo = params[0].split("=")[1];
            String fullName = params[1].split("=")[1];
            String username = params[2].split("=")[1];
            String password = params[3].split("=")[1];

            return new ConfirmationRequest(emailTo, fullName, username, password);
        } else {
            throw new CustomException(APIStatus.EMAIL_SEND_FAILED);
        }
    }
}
