package com.lguplus.ukids.admin.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    private static final String CLASS_LOG_FORMAT = "Class Name : [";
    private static final String METHOD_LOG_FORMAT = "Method Name : [";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.lguplus.ukids.admin.exception..*(..))"
            + " or execution(* com.lguplus.ukids.admin.interceptor..*(..))"
            // + " or execution(* com.lguplus.ukids.admin.repository..*(..))"
            + " or execution(* com.lguplus.ukids.admin.restcontroller..*(..))"
            + " or execution(* com.lguplus.ukids.admin.service..*(..))")
    public void beforeMethod(final JoinPoint joinPoint) {
        StringBuilder beforeMethodInfo = new StringBuilder();
        String logInfo = beforeMethodInfo.append("Before method :").append(CLASS_LOG_FORMAT)
                .append(joinPoint.getSignature().getDeclaringTypeName()).append("], ").append(METHOD_LOG_FORMAT)
                .append(joinPoint.getSignature().getName()).append("], ").append("Data : [")
                .append(Arrays.toString(joinPoint.getArgs())).append("]").toString();

        LOGGER.info(logInfo);
    }

    @AfterReturning(pointcut = "(execution(* com.lguplus.ukids.admin.exception..*(..))"
            + " or execution(* com.lguplus.ukids.admin.interceptor..*(..))"
            // + " or execution(* com.lguplus.ukids.admin.repository..*(..))"
            + " or execution(* com.lguplus.ukids.admin.restcontroller..*(..))"
            + " or execution(* com.lguplus.ukids.admin.service..*(..)))"
            + " && !@annotation(com.lguplus.ukids.admin.annotation.NoLogging)", returning = "result")
    public void afterMethod(final JoinPoint joinPoint, final Object result) {
        StringBuilder afterMethodInfo = new StringBuilder();
        String logInfo = afterMethodInfo.append("After  method :").append(CLASS_LOG_FORMAT)
                .append(joinPoint.getSignature().getDeclaringTypeName()).append("], ").append(METHOD_LOG_FORMAT)
                .append(joinPoint.getSignature().getName()).append("], ").append("Data : [").append(result).append("]")
                .toString();

        LOGGER.info(logInfo);
    }

    @AfterThrowing(pointcut = "execution(* com.lguplus.ukids.admin.interceptor..*(..))"
            + " or execution(* com.lguplus.ukids.admin.repository..*(..))"
            + " or execution(* com.lguplus.ukids.admin.restcontroller..*(..))"
            + " or execution(* com.lguplus.ukids.admin.service..*(..))", throwing = "ex")
    public void afterThrowing(final JoinPoint joinPoint, final Exception ex) {
        StringBuilder afterThrowingInfo = new StringBuilder();
        String logInfo = afterThrowingInfo.append("This method Throwing :").append(CLASS_LOG_FORMAT)
                .append(joinPoint.getSignature().getDeclaringTypeName()).append("], ").append(METHOD_LOG_FORMAT)
                .append(joinPoint.getSignature().getName()).append("], ").append("Excpetion : [")
                .append(ex.getMessage()).append("]").toString();

        LOGGER.info(logInfo);
    }
}
