package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleComTitulosRecord;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoOracleService {
  Optional<ClienteOracle> pesquisarClienteOracle(final String codigoErp);

  Page<@NonNull ContratoOracleComTitulosRecord> pesquisarContratos(
      final String empresa,
      final String centroDeCusto,
      final Pageable pageable,
      final AtomicInteger contador);
}
