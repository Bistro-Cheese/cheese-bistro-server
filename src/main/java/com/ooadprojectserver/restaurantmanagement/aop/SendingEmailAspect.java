package com.ooadprojectserver.restaurantmanagement.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 5/13/2024, Monday
 * @description:
 **/
@Aspect
@Component
public class SendingEmailAspect {

    private final Logger logger = Logger.getLogger(SendingEmailAspect.class.getName());

    @After("execution(* com.ooadprojectserver.restaurantmanagement.controller.UserController.createUser(..))")
    public void afterCreateUserMethod(JoinPoint joinPoint){
        logger.info("ASPECT EMAIL");
    }

}
