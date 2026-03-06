package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.settings.MessengerSettingsDto;
import br.com.arenco.arenco_clientes.entities.MessengerSettingsModel;

public record MessengerSettingsDtoFactory(MessengerSettingsModel model) {
  public MessengerSettingsDto create() {
    final MessengerSettingsDto dto = new MessengerSettingsDto();
    dto.setId(model.getId());
    dto.setState(model.getState());
    dto.setStrategy(model.getStrategy());
    dto.setVersion(model.getVersion());
    dto.setTargetUsers(model.getTargetUsers());
    return dto;
  }
}
