package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.EmailModel;
import br.com.arenco.arenco_clientes.dtos.EmailDto;

public record EmailDtoFactory(EmailModel model) {
  public EmailDto create() {
    return new EmailDto(
        ArencoDateUtils.fromInstantToString(model.getDateCreated()),
        model.getFrom(),
        model.getSubject(),
        model.getMessageId(),
        model.getTemplateId(),
        model.getDeliveredTo(),
        model.getStatusCode());
  }
}
