package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.AgreementModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoCustomerContratosService {
  Page<AgreementModel> buscarContratos(final Pageable pageable);
}
