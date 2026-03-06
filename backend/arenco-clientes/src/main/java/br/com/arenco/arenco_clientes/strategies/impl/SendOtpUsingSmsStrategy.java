package br.com.arenco.arenco_clientes.strategies.impl;

import br.com.arenco.arenco_clientes.config.properties.MessengerProperties;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.enums.ContactType;
import br.com.arenco.arenco_clientes.services.ArencoSmsService;
import br.com.arenco.arenco_clientes.strategies.SendOtpStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOtpUsingSmsStrategy implements SendOtpStrategy {
  private final MessengerProperties messengerProperties;
  private final ArencoSmsService arencoSmsService;

  @Override
  public boolean apply(final ContactModel contact) {
    return contact.getContactType() == ContactType.CELLPHONE;
  }

  @Override
  public void sendToken(final String name, final String to, final String token) {
    final var data = String.format("Arenco - Olá, %s. Seu código: %s", name, token);
    arencoSmsService.sendSms(to, data, messengerProperties.getTwilioFrom());
  }
}
