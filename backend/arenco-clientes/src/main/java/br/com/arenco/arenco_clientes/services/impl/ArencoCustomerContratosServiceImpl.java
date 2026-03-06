package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoSessionService;
import br.com.arenco.arenco_clientes.services.ArencoCustomerContratosService;
import br.com.arenco.arenco_clientes.entities.AgreementModel;
import br.com.arenco.arenco_clientes.repositories.AgreementModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoCustomerContratosServiceImpl implements ArencoCustomerContratosService {
  private final AgreementModelRepository repository;
  private final ArencoSessionService arencoSessionService;

  @Override
  public Page<AgreementModel> buscarContratos(final Pageable pageable) {
    final var currentUser = arencoSessionService.getCurrentUser();
    return repository.findAllByUserId(currentUser.getId(), pageable);
  }
}
