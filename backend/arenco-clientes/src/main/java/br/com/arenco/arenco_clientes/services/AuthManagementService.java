package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.SessionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthManagementService {
  Page<SessionModel> findAllSessions(final Pageable pageable);

  void removeSession(final String id);
}
