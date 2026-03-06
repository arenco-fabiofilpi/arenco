package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.PasswordRecoveryOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.time.Instant;

public interface PasswordRecoveryOtpService {
  PasswordRecoveryOtpModel create(final UserModel userModel, final ContactModel contactModel, final Instant expiresAt);

  void validate(final String otpId, final String token);
}
