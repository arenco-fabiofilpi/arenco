package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.AdminUsersService;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.UserType;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUsersServiceImpl implements AdminUsersService {
  private final UserModelRepository userModelRepository;

  @Override
  public Page<UserModel> findAllAdminUsers(final Pageable pageable) {
    return userModelRepository.findAllByUserType(UserType.ADMINISTRADOR, pageable);
  }

  @Override
  public UserModel findAdminUser(final String id) {
    return userModelRepository
        .findByIdAndUserType(id, UserType.ADMINISTRADOR)
        .orElseThrow(() -> new IdNotFoundException("Nao encontrado usuário administrador"));
  }

  @Override
  public void removeAdminUser(final UserModel user) {
    log.info("Removendo Usuário administrador {}", user.getUsername());
    userModelRepository.delete(user);
  }
}
