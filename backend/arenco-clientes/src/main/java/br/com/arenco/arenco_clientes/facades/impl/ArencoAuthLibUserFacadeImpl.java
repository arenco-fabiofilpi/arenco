package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ArencoAuthLibUserFacade;
import br.com.arenco.arenco_clientes.services.ArencoAuthLibContactService;
import br.com.arenco.arenco_clientes.services.ArencoPreSessionService;
import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.factories.ContactDtoFactory;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoAuthLibUserFacadeImpl implements ArencoAuthLibUserFacade {
  private final ArencoPreSessionService arencoPreSessionService;
  private final ArencoAuthLibContactService arencoAuthLibContactService;

  @Override
  public List<ContactDto> getContactsForOtpSend() {
    final var userModel = arencoPreSessionService.getUserModelFromPreSessionContext();
    return getCurrentUserContacts(userModel, true);
  }

  @Override
  public List<ContactDto> getCurrentUserContacts(
      final UserModel userModel, final boolean maskValues) {
    final var contacts = arencoAuthLibContactService.findByUser(userModel);
    log.info("getCurrentUserContacts - Converting ListOfUserContactDto: {}", contacts);
    final var list = new ArrayList<ContactDto>();
    for (final var contact : contacts) {
      final var contactDto = new ContactDtoFactory(contact, maskValues).create();
      list.add(contactDto);
    }
    return list;
  }
}
