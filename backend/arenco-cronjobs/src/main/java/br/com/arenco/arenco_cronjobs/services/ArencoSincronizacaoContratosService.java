package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import br.com.arenco.arenco_cronjobs.entities.UserModel;
import java.util.List;

public interface ArencoSincronizacaoContratosService {
    List<BoletoAProcessarModel> sincronizarContrato(
      final UserModel clienteModel,
      final ContratoOracle contratoOracle,
      final List<TituloAReceberOracle> tituloAReceberOracleList,
      final List<TituloRecebidoOracle> tituloRecebidoOracleList);
}
