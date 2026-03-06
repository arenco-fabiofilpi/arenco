package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import br.com.arenco.arenco_cronjobs.entities.AgreementModel;
import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import java.util.List;

public interface ArencoSincronizacaoTitulosService {
  void sincronizarTitulos(
      final AgreementModel contratoMongo,
      final List<TituloAReceberOracle> titulosAReceber,
      final List<TituloRecebidoOracle> titulosRecebidos);

  void baixarTitulosRecebidos(
      final AgreementModel contratoMongo,
      final List<TituloAReceberOracle> tituloAReceberOracles,
      final List<BoletoAProcessarModel> boletosAProcessar);
}
