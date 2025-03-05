package uz.dizgo.erp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // O'zgartirilishi mumkin
        executor.setMaxPoolSize(20);   // O'zgartirilishi mumkin
        executor.setQueueCapacity(100); // O'zgartirilishi mumkin
        executor.setThreadNamePrefix("async-thread-");
        executor.initialize();
        return executor;
    }
}
