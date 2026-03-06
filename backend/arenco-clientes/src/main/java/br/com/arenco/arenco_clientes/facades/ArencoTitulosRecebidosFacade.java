package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoCompletoDto;
import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoTitulosRecebidosFacade {

  Page<TituloRecebidoDto> pesquisarTitulosRecebidos(final Pageable pageable);

  Optional<TituloRecebidoCompletoDto> pegarTituloRecebido(final String id);
}
