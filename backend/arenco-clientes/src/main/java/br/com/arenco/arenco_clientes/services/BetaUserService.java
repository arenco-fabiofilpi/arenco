package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.BetaUserModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BetaUserService {
  UserModel findUserModel(final String userModelId);

  Page<BetaUserModel> findAll(final Pageable pageable);

  void addBetaUser(final String userId);

  boolean existsByUserId(final String userId);

  void removeBetaUser(final String betaUserId);
}
