package br.com.arenco.arenco_cronjobs.strategies;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.exceptions.ArencoSincronizacaoBoletosInterrompida;

public interface GerarBoletoStrategy {
  void gerar(
      final UserModel customer,
      final AddressModel addressModel,
      final ReceivableTitleModel receivableTitleModel,
      final PaymentSlipSettingsModel paymentSlipSettingsModel)
      throws ArencoSincronizacaoBoletosInterrompida;
}
