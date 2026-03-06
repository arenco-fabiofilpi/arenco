package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.MessengerStrategyNotFound;
import br.com.arenco.arenco_clientes.services.OTPMessengerService;
import br.com.arenco.arenco_clientes.strategies.SendOtpStrategy;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPMessengerServiceImpl implements OTPMessengerService {
  private final List<SendOtpStrategy> strategies;

  @Override
  public void send(final String name, final ContactModel contactModel, final String token) {
    for (final SendOtpStrategy strategy : strategies) {
      if (strategy.apply(contactModel)) {
        strategy.sendToken(name, contactModel.getValue(), token);
        return;
      }
    }
    throw new MessengerStrategyNotFound(
        "Strategy to send OTP not found. Contact Model: " + contactModel.getId());
  }
}
