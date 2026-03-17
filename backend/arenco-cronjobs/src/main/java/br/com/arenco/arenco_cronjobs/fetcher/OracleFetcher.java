package br.com.arenco.arenco_cronjobs.fetcher;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleComTitulosRecord;
import java.util.List;
import java.util.Map;

public interface OracleFetcher {
  Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> buscarContratosEClientesOracle();
}
