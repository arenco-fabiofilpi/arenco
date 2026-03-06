package br.com.arenco.arenco_cronjobs.listeners;

import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupEventListener {
  @EventListener(ApplicationReadyEvent.class)
  public void started() {
    setTimeZone();
  }

  private void setTimeZone() {
    log.info("Setting default timezone to America/Sao_Paulo");
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
  }
}
