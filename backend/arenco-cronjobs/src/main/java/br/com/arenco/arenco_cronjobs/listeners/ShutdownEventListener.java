package br.com.arenco.arenco_cronjobs.listeners;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

  private volatile boolean shuttingDown = false;

  @Override
  public void onApplicationEvent(@Nonnull final ContextClosedEvent event) {
    shuttingDown = true;
  }
}
