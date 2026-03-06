package br.com.arenco.arenco_clientes.strategies;

import br.com.arenco.arenco_clientes.entities.ContactModel;

public interface SendOtpStrategy {
  boolean apply(final ContactModel contact);

  void sendToken(final String name, final String to, final String token);
}
