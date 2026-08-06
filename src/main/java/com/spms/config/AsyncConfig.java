package com.spms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// Configures a thread pool to execute background tasks (e.g., sending OTP emails) asynchronously.
@Configuration // Marks this as a Spring configuration class
@EnableAsync  // Enables asynchronous (@Async) methods
public class AsyncConfig {

    @Bean(name = "mailExecutor") // Creates a custom thread pool bean
    public Executor mailExecutor() {

        // Creates a thread pool for background tasks
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2); // Always keep 2 threads ready

        executor.setMaxPoolSize(4); // Can create up to 4 threads if needed

        executor.setQueueCapacity(100); // Holds up to 100 waiting tasks

        executor.setThreadNamePrefix("mail-"); // Thread names: mail-1, mail-2...

        executor.initialize(); // Starts the thread pool

        return executor; // Makes the executor available to Spring
    }
}