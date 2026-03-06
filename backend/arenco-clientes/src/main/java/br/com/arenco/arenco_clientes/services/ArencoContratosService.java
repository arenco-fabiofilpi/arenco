package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.AgreementModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoContratosService {
  Optional<AgreementModel> findById(final String uid);

  Page<AgreementModel> pesquisar(final Pageable pageable);
}
