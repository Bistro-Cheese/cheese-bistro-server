package com.ooadprojectserver.restaurantmanagement.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/13/2024, Monday
 * @description:
 **/

@Aspect
@Component
public class LoggingServiceAspect {

    private final Logger logger = Logger.getLogger(LoggingServiceAspect.class.getName());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void  controllerMethods(){}

    @Around("controllerMethods()")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

        logger.info("User activity detected from IP address:  " + remoteAddress + " for method: " + methodName);

        return joinPoint.proceed();
    }

}
