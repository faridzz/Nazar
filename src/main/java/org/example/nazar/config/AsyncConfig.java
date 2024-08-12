package org.example.nazar.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {


    @Bean(name = "addReviewExecutor")
    public Executor saveReviewExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(40); // تنظیم تعداد پایه تردها
        executor.setMaxPoolSize(80); // تنظیم حداکثر تعداد تردها
        executor.setQueueCapacity(8000); // تنظیم ظرفیت صف
        executor.setThreadNamePrefix("Async-"); // تنظیم پیشوند نام تردها
        executor.initialize(); // مقداردهی اولیه به executor
        return executor; // بازگشت executor به عنوان Bean
    }
}
