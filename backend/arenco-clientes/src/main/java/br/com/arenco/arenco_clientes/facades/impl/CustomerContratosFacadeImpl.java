package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.CustomerContratosFacade;
import br.com.arenco.arenco_clientes.services.ArencoCustomerContratosService;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import br.com.arenco.arenco_clientes.factories.ContratoDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerContratosFacadeImpl implements CustomerContratosFacade {
  private final ArencoCustomerContratosService arencoCustomerContratosService;

  @Override
  public Page<ContratoDto> pesquisarContratos(final Pageable pageable) {
    final var modelPage = arencoCustomerContratosService.buscarContratos(pageable);
    return modelPage.map(i -> new ContratoDtoFactory(i).create());
  }
}
