package com.ooadprojectserver.restaurantmanagement.aop;

import com.ooadprojectserver.restaurantmanagement.model.aws.UserAction;
import com.ooadprojectserver.restaurantmanagement.service.aws.AWSDynamoDbService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/13/2024, Monday
 * @description:
 **/

@Aspect
@Component
@RequiredArgsConstructor
public class TracingControllerAspect {

    private final Logger logger = Logger.getLogger(TracingControllerAspect.class.getName());
    private final UserDetailService userDetailService;
    private final AWSDynamoDbService awsDynamoDbService;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

        if (userDetailService.getIdLogin() != null) {
            UserAction userAction = UserAction.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(userDetailService.getIdLogin().toString())
                    .userName(userDetailService.getUsernameLogin())
                    .methodName(methodName)
                    .build();

            awsDynamoDbService.putUserAction(userAction);
        }

        return joinPoint.proceed();
    }

}
