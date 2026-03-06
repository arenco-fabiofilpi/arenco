package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.dtos.SessionDto;
import br.com.arenco.arenco_clientes.facades.AuthManagementFacade;
import br.com.arenco.arenco_clientes.factories.SessionDtoFactory;
import br.com.arenco.arenco_clientes.services.AuthManagementService;
import br.com.arenco.arenco_clientes.services.ArencoUserAuthService;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthManagementFacadeImpl implements AuthManagementFacade {
  private final AuthManagementService authManagementService;
  private final ArencoUserAuthService arencoUserAuthService;

  @Override
  public Page<SessionDto> getActiveSessions(final Pageable pageable) {
    final var modelPage = authManagementService.findAllSessions(pageable);
    return modelPage.map(
        model -> {
          final var userId = model.getUserId();
          final var userModel =
              arencoUserAuthService.findUserById(userId, IdNotFoundException.class);
          return new SessionDtoFactory(model, userModel.getUsername()).create();
        });
  }

  @Override
  public void removeActiveSession(final String id) {
    authManagementService.removeSession(id);
    log.info(String.format("Removed active session with id %s", id));
  }
}
