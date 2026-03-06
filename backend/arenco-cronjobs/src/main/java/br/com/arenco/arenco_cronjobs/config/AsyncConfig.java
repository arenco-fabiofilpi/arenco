package br.com.arenco.arenco_cronjobs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

  @Bean(name = "syncExecutor")
  public ThreadPoolTaskExecutor syncExecutor() {
    var exec = new ThreadPoolTaskExecutor();
    exec.setCorePoolSize(2); // ajuste conforme a sua carga
    exec.setMaxPoolSize(4);
    exec.setQueueCapacity(100);
    exec.setThreadNamePrefix("sync-");
    exec.initialize();
    return exec;
  }
}
