package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.settings.MessengerSettingsDto;
import br.com.arenco.arenco_clientes.dtos.UpdateMessengerSettingsDto;

public interface ArencoMessengerSettingsFacade {
  MessengerSettingsDto getSettings();

  void updateSettings(final UpdateMessengerSettingsDto updateSettingsDto);
}
