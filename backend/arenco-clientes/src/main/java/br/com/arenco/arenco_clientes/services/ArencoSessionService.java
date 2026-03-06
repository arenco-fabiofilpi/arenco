package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.dtos.authentication.SessionRecord;
import br.com.arenco.arenco_clientes.entities.UserModel;

public interface ArencoSessionService {
  SessionRecord createSession(final String userId);

  UserModel getUserBySessionId(final String sessionId);

  UserModel getCurrentUser();

  void removeSession(final String sessionId);
}
