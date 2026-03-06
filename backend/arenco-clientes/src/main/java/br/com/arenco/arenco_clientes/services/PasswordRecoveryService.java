package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.PasswordRecoveryModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public interface PasswordRecoveryService {
  PasswordRecoveryModel create(final UserModel user);

  PasswordRecoveryModel getPasswordRecoveryModel(final String userId);

  void updateOtpId(final PasswordRecoveryModel passwordRecoveryModel, final String otpId);

  void remove(final PasswordRecoveryModel passwordRecoveryModel);
}
