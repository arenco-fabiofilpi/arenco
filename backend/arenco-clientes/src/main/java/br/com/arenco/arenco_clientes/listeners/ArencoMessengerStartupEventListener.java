package br.com.arenco.arenco_clientes.listeners;

import br.com.arenco.arenco_clientes.services.ArencoSmsService;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoMessengerStartupEventListener {
  private final ArencoSmsService arencoSmsService;

  @EventListener(ApplicationReadyEvent.class)
  public void started() {
    setTimeZone();
    arencoSmsService.registerTwilioAccount();
  }

  private void setTimeZone() {
    log.info("Setting default timezone to America/Sao_Paulo");
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
  }
}
