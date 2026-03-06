package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoTitulosRecebidosService {
  Optional<ReceivedTitleModel> findById(final String uid);

  Page<ReceivedTitleModel> pesquisar(final Pageable pageable);
}
