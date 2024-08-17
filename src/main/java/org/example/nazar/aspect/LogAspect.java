package org.example.nazar.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.example.nazar.dto.ReviewResultDTO;

import org.example.nazar.util.time.nowtime.CurrentTimeProvider;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("execution(* org.example.nazar.service.scraper.IAddReviewToDataBase.*(..))")
    @SuppressWarnings("unchecked")
    public ReviewResultDTO measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        CurrentTimeProvider currentTimeProvider = new CurrentTimeProvider();
        String currentDate = currentTimeProvider.get();
        ReviewResultDTO result = null;
        try {
            // اجرای متد
            result = (ReviewResultDTO) joinPoint.proceed();
        } finally {
            double executionTime = System.currentTimeMillis() - start;

            if (result == null) {
                log.warn("Error {} : {} executed in: {} ms, but returned null",
                        currentDate,
                        joinPoint.getSignature(),
                        executionTime);

            } else {
                log.info("{} : {} executed {} reviews in: {} ms , duplicated results is {} and speed: {} reviews/second ",
                        currentDate,
                        joinPoint.getSignature(),
                        result.getReviewsNumber(),
                        executionTime,
                        result.getDuplicateCount(),
                        result.getReviewsNumber() / (executionTime / 1000));
            }
        }

        return result;
    }
}