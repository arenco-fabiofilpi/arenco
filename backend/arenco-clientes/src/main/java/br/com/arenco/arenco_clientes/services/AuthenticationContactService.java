package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public interface AuthenticationContactService {
  ContactModel getPreferredContact(final UserModel userModel);
}
