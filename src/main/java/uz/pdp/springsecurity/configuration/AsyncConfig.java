package uz.pdp.springsecurity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // Bu Spring Bootda asinxron ishlashni yoqadi
public class AsyncConfig implements AsyncConfigurer {

    // Thread poolni yaratish
    @Override
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Boshlang'ich ishchi to'plami soni
        executor.setMaxPoolSize(20);  // Maksimal ishchi to'plami soni
        executor.setQueueCapacity(500); // Ishchi navbatlarining maksimal o'lchami
        executor.setThreadNamePrefix("Async-Executor-"); // Nom berish
        executor.initialize();
        return executor;
    }
}
