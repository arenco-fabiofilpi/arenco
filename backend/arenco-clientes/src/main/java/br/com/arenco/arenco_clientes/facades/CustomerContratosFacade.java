package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerContratosFacade {
  Page<ContratoDto> pesquisarContratos(final Pageable pageable);
}
