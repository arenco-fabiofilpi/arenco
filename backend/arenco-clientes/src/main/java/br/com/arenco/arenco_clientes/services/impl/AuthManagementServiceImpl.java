package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.AuthManagementService;
import br.com.arenco.arenco_clientes.entities.SessionModel;
import br.com.arenco.arenco_clientes.repositories.SessionModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthManagementServiceImpl implements AuthManagementService {
  private final SessionModelRepository sessionModelRepository;

  @Override
  public Page<SessionModel> findAllSessions(final Pageable pageable) {
    return sessionModelRepository.findAll(pageable);
  }

  @Override
  public void removeSession(final String id) {
    sessionModelRepository.deleteById(id);
  }
}
