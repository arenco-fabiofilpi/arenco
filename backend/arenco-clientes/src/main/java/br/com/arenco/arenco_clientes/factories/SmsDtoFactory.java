package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.SMSModel;
import br.com.arenco.arenco_clientes.dtos.SmsDto;

public record SmsDtoFactory(SMSModel model) {
  public SmsDto create() {
    return new SmsDto(
        model.getFrom(),
        model.getDeliveredTo(),
        model.getData(),
        model.getStatus(),
        ArencoDateUtils.fromInstantToString(model.getDateCreated()));
  }
}
