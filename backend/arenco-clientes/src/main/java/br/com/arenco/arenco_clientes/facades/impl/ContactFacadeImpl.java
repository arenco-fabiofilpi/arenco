package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ContactFacade;
import br.com.arenco.arenco_clientes.services.contact.ContactService;
import br.com.arenco.arenco_clientes.services.users.UserService;
import br.com.arenco.arenco_clientes.dtos.user.ContactFullDto;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.factories.ContactFullDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactFacadeImpl implements ContactFacade {
  private final ContactService contactService;
  private final UserService userService;

  @Override
  public Page<ContactFullDto> search(final Pageable pageable) {
    final var modelPage = contactService.findAll(pageable);
    return modelPage.map(
        model -> {
          final var userId = model.getUserModelId();
          final var userModel = userService.findUserById(userId, IdNotFoundException.class);
          return new ContactFullDtoFactory(model, userModel).create();
        });
  }
}
