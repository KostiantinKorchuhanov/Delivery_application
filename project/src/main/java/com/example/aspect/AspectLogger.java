package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;



@Aspect
@Slf4j
public class AspectLogger {
    @Before("execution(* com.example..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("IN: {} | My cute args: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.example..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.debug("<<<<< Out: {} | My cute results: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    @AfterThrowing(pointcut = "execution(* com.example..*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Throwable ex) {
        log.error("!!! HOHOHO ERROR: {} | Message to you: {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage(), ex);
    }
}
