package com.shoppingcartservice.logger;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(public * com.shoppingcartservice.service.impl"
            + ".ShoppingCartServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("Method was called: {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Parameters {}", Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
            LOGGER.info("Method return {}", result);
        } catch (Exception exception) {
            LOGGER.error("Occurred exception {}", joinPoint.getSignature().getName());
            throw exception;
        }
        return result;
    }
}
