package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.entities.UserModel;

import java.util.List;

public interface ArencoAuthLibUserFacade {
  List<ContactDto> getCurrentUserContacts(final UserModel userModel, final boolean maskValues);

  List<ContactDto> getContactsForOtpSend();
}
