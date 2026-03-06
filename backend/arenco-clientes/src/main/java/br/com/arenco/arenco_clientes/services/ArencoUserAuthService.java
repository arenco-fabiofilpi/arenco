package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.UserModel;

import java.util.Optional;

public interface ArencoUserAuthService {
  UserModel findUserById(final String id, Class<? extends Exception> exceptionClass);

  Optional<UserModel> findUserByUsername(final String username);

  UserModel findUserByUsername(final String username, Class<? extends Exception> exceptionClass);

  void setNewPassword(final UserModel user, final String password);

  void authenticateUserAndPassword(final UserModel userModel, final String password);
}
