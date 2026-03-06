package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.SessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthManagementFacade {
  Page<SessionDto> getActiveSessions(final Pageable pageable);

    void removeActiveSession(final String id);
}
