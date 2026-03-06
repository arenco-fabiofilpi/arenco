package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import br.com.arenco.arenco_cronjobs.enums.TipoProcessamentoBoleto;
import br.com.arenco.arenco_cronjobs.exceptions.ArencoSincronizacaoBoletosInterrompida;
import org.springframework.transaction.annotation.Transactional;

public interface BoletosService {
  BoletoAProcessarModel gerarBoletoAProcessar(
      final TipoProcessamentoBoleto tipoProcessamentoBoleto, final String receivableTitleId);

  @Transactional(noRollbackFor = ArencoSincronizacaoBoletosInterrompida.class)
  void processarBoletos() throws ArencoSincronizacaoBoletosInterrompida;
}
