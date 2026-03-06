package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoTitulosAReceberService {
  Optional<ReceivableTitleModel> findById(final String uid);

  Page<ReceivableTitleModel> pesquisar(final Pageable pageable);
}
