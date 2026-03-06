package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ContactPreferenceFacade;
import br.com.arenco.arenco_clientes.services.contact.ContactPreferenceService;
import br.com.arenco.arenco_clientes.services.contact.ContactService;
import br.com.arenco.arenco_clientes.services.users.UserService;
import br.com.arenco.arenco_clientes.dtos.user.ContactPreferenceDto;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.factories.ContactPreferenceDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactPreferenceFacadeImpl implements ContactPreferenceFacade {

  private final UserService userService;
  private final ContactService contactService;
  private final ContactPreferenceService contactPreferenceService;

  @Override
  public Page<ContactPreferenceDto> search(final Pageable pageable) {
    final var modelPage = contactPreferenceService.findAll(pageable);
    return modelPage.map(
        model -> {
          final var userId = model.getUserModelId();
          final var userModel = userService.findUserById(userId, IdNotFoundException.class);
          final var contactModel =
              contactService.findById(model.getContactModelId(), IdNotFoundException.class);
          return new ContactPreferenceDtoFactory(model, contactModel, userModel).create();
        });
  }
}
