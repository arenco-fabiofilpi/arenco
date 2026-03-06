package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.AdminUsersFacade;
import br.com.arenco.arenco_clientes.services.AdminUsersService;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.factories.UserDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUsersFacadeImpl implements AdminUsersFacade {
  private final AdminUsersService service;

  @Override
  public Page<UserDto> findAll(final Pageable pageable) {
    final var modelPage = service.findAllAdminUsers(pageable);
    return modelPage.map(model -> new UserDtoFactory(model).create());
  }

  @Override
  public void removeAdminUser(final String id) {
    final var adminUser = service.findAdminUser(id);
    service.removeAdminUser(adminUser);
  }
}
