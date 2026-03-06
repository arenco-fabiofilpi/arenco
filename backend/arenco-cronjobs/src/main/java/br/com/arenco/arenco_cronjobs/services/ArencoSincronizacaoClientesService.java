package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.entities.UserModel;

public interface ArencoSincronizacaoClientesService {
  UserModel replicateCustomerFromOracleToMongo(final ClienteOracle clienteOracle);
}
