package org.example.nazar.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.example.nazar.dto.ReviewResultDto;
import org.example.nazar.model.ProductReview;

import org.example.nazar.util.time.nowtime.CurrentTimeProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("execution(* org.example.nazar.service.scraper.IAddReviewToDataBase.*(..))")
    @SuppressWarnings("unchecked")
    public ReviewResultDto measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        CurrentTimeProvider currentTimeProvider = new CurrentTimeProvider();
        String currentDate = currentTimeProvider.get();
        ReviewResultDto result = null;
        try {
            // اجرای متد
            result = (ReviewResultDto) joinPoint.proceed();
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