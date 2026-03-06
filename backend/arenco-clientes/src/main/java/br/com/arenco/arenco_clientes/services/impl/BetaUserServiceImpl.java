package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.entities.BetaUserModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.BetaUserModelRepository;
import br.com.arenco.arenco_clientes.repositories.UserModelRepository;
import br.com.arenco.arenco_clientes.services.BetaUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetaUserServiceImpl implements BetaUserService {
  private final BetaUserModelRepository repository;
  private final UserModelRepository userRepository;

  @Override
  public UserModel findUserModel(final String userModelId) {
    return userRepository
        .findById(userModelId)
        .orElseThrow(
            () -> new IdNotFoundException("Nao Encontrado Usuario para o ID " + userModelId));
  }

  @Override
  public Page<BetaUserModel> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public boolean existsByUserId(final String userId) {
    return repository.existsByUserModelId(userId);
  }

  @Override
  public void addBetaUser(final String userId) {
    final var betaUserModel = new BetaUserModel();
    betaUserModel.setUserModelId(userId);
    repository.save(betaUserModel);
  }

  @Override
  public void removeBetaUser(final String betaUserId) {
    final var betaUserModelOptional = repository.findById(betaUserId);
    if (betaUserModelOptional.isPresent()) {
      repository.delete(betaUserModelOptional.get());
      log.info("Removed Messenger Settings Beta User {}", betaUserId);
      return;
    }
    log.warn("No beta user found with id {}", betaUserId);
  }
}
