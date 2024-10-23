package com.userservice.logger;

import java.lang.reflect.Field;
import java.util.Arrays;
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

    @Around("execution(public * com.userservice.security.AuthenticationService.*(..)) "
            + "|| execution(public * com.userservice.service.impl.UserServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("Method call: {}", joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();

        Object[] filteredArgs = Arrays.stream(args)
                .map(this::createMaskedCopy).toArray();

        LOGGER.info("Parameter: {}", Arrays.toString(filteredArgs));
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error("Method return: {}", throwable.getMessage());
            throw throwable;
        }
        LOGGER.info("Return: {}", result);
        return result;
    }

    private Object createMaskedCopy(Object arg) {
        try {
            Object maskedObject = arg.getClass().getDeclaredConstructor().newInstance();
            for (Field field : arg.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Sensitive.class)) {
                    field.set(maskedObject, "****");
                } else {
                    field.set(maskedObject, field.get(arg));
                }
            }
            return maskedObject;
        } catch (Exception exception) {
            LOGGER.error("Error during masking sensitive data: {}", exception.getMessage());
            return arg;
        }
    }
}
