package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;

import java.util.Optional;

public interface PreSessionOtpService {
  PreSessionOtpModel createOneTimePassword(final ContactModel contactModel);

  void validateOneTimePassword(final String oneTimePassword);

  void authenticateOneTimePassword(final String oneTimePassword);

  Optional<PreSessionOtpModel> findByPreSessionId(final String preSessionId);
}
