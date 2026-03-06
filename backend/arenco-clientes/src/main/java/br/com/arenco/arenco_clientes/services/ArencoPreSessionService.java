package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedPreSessionContext;
import br.com.arenco.arenco_clientes.dtos.authentication.PreSessionRecord;
import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public interface ArencoPreSessionService {
  PreSessionRecord createPreSession(final String userId);

  Optional<AuthenticatedPreSessionModel> findPreSessionModel(final String preSessionId);

  UserModel getUserBySessionModel(final AuthenticatedPreSessionModel authenticatedPreSessionModel);

  ArencoAuthenticatedPreSessionContext getCurrentPreSessionContext();

  UserModel getUserModelFromPreSessionContext(
      final ArencoAuthenticatedPreSessionContext preSessionContext);

  UserModel getUserModelFromPreSessionContext();

  AuthenticatedPreSessionModel getPreSessionModelFromPreSessionContext(
          final ArencoAuthenticatedPreSessionContext preSessionContext);

  AtomicReference<PreSessionOtpModel> getOtpFromPreSessionContext(
          final ArencoAuthenticatedPreSessionContext preSessionContext);

  void removePreSession(final String id);
}
