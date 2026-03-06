package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.List;

public interface ArencoAuthLibContactService {
  List<ContactModel> findByUser(final UserModel user);
}
