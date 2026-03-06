package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;

public interface OTPMessengerService {
  void send(final String name, final ContactModel contactModel, final String token);
}
