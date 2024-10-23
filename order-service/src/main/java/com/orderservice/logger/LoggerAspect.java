package com.orderservice.logger;

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

    @Around("execution(public * com.orderservice.service.impl.OrderServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("Method called {}", joinPoint.getSignature().getName());
        LOGGER.info("Parameters {}", joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();
            LOGGER.info("Returned result {}", result);
        } catch (Throwable exaption) {
            LOGGER.error("During proceed occurred exception", exaption);
            throw exaption;
        }
        return result;
    }
}
