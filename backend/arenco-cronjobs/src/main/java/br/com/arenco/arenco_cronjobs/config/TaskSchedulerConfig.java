package br.com.arenco.arenco_cronjobs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@Configuration
public class TaskSchedulerConfig {
  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    var ts = new ThreadPoolTaskScheduler();
    ts.setPoolSize(4);
    ts.setThreadNamePrefix("sched-");
    ts.setErrorHandler(t -> log.error("Erro em tarefa agendada", t));
    ts.setWaitForTasksToCompleteOnShutdown(true);
    ts.setAwaitTerminationSeconds(30);
    ts.setRemoveOnCancelPolicy(true);
    return ts;
  }
}
