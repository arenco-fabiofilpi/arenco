package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.dtos.settings.BetaUserDto;
import br.com.arenco.arenco_clientes.factories.BetaUserDtoFactory;
import br.com.arenco.arenco_clientes.services.BetaUserService;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerBetaUsersFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoMessengerBetaUsersFacadeImpl implements ArencoMessengerBetaUsersFacade {
  private final BetaUserService service;

  @Override
  public Page<BetaUserDto> findAll(final Pageable pageable) {
    final var models = service.findAll(pageable);
    return models.map(
        i -> {
          final var userModelId = i.getUserModelId();
          final var userModel = service.findUserModel(userModelId);
          return new BetaUserDtoFactory(i, userModel).create();
        });
  }

  @Override
  public HttpStatus addBetaUser(final String userModelId) {
    final var alreadyExists = service.existsByUserId(userModelId);
    if (alreadyExists) {
      log.info("User with id {} already exists at Beta Users table", userModelId);
      return HttpStatus.ALREADY_REPORTED;
    }
    final var userModel = service.findUserModel(userModelId);
    log.info("Adding UserModel {} to Beta Users", userModel.getUsername());
    service.addBetaUser(userModelId);
    return HttpStatus.CREATED;
  }

  @Override
  public void removeBetaUser(final String betaUserId) {
    service.removeBetaUser(betaUserId);
  }
}
