package org.example.nazar.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ErrorAspect {

    private static final int MAX_RETRIES = 3;

    @Around("execution(* org.jsoup.Connection.get(..)) || execution(* org.example.nazar.util.network.IHttpRequestSender.*(..))")
    public Object retryConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        int attempt = 0;
        Throwable lastException = null;

        while (attempt < MAX_RETRIES) {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                lastException = throwable;
                attempt++;
                log.warn("Error Retry attempt : {} connecting again ...", attempt);
            }
        }

        throw lastException;
    }
}