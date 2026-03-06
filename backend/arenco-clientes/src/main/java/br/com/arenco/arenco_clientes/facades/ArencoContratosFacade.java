package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.agreements.ContratoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoContratosFacade {
  Page<ContratoDto> pesquisarContratos(final Pageable pageable);

  Optional<ContratoCompletoDto> pegarContratoPorId(final String id);
}
