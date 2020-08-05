package org.codejudge.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan({"org.codejudge.sb"})
@Slf4j
@EnableAsync
public class Application {

    public static void main(String[] args) {
        log.info("Starting Application...");
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "customThreadPool")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.initialize();
        return executor;
    }
}
