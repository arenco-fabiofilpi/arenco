package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.MessengerSettingsModel;

public interface MessengerSettingsService {
  MessengerSettingsModel get();

  void save(final MessengerSettingsModel model);
}
