package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoTitulosAReceberFacade {
  Page<TituloAReceberDto> pesquisarTitulosAReceber(final Pageable pageable);

  TituloAReceberCompletoDto pegarTituloAReceber(final String id);
}
