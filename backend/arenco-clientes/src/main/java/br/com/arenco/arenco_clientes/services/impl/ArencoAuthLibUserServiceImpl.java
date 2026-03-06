package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoAuthLibUserService;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoAuthLibUserServiceImpl implements ArencoAuthLibUserService {
  private final UserModelRepository userModelRepository;

  @Override
  public Optional<UserModel> findById(final String id) {
    if (id == null) {
      return Optional.empty();
    }
    return userModelRepository.findById(id);
  }
}
