package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.ArencoContratosService;
import br.com.arenco.arenco_clientes.entities.AgreementModel;
import br.com.arenco.arenco_clientes.repositories.AgreementModelRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoContratosServiceImpl implements ArencoContratosService {
  private final AgreementModelRepository repository;

  @Override
  public Optional<AgreementModel> findById(final String id) {
    if (id == null) {
      return Optional.empty();
    }
    return repository.findById(id);
  }

  @Override
  public Page<AgreementModel> pesquisar(final Pageable pageable) {
    return repository.findAll(pageable);
  }
}
