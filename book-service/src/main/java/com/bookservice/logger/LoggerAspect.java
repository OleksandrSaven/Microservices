package com.bookservice.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

    @Around("execution(public * com.bookservice.service.impl.BookServiceImpl.*(..)) "
            + "|| execution(public * com.bookservice.service.impl.CategoryServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("Method call: {}", joinPoint.getSignature().toLongString());
        LOGGER.info("Parameters: {}", joinPoint.getArgs());
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable exception) {
            LOGGER.error("The method threw an exception: {}", exception.getMessage());
            throw exception;
        }
        LOGGER.info("Result: {}", result);
        return result;
    }
}
