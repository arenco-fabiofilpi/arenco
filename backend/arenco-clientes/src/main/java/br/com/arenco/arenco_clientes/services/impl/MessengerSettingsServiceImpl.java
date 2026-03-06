package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.entities.MessengerSettingsModel;
import br.com.arenco.arenco_clientes.repositories.MessengerSettingsModelRepository;
import br.com.arenco.arenco_clientes.services.MessengerSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessengerSettingsServiceImpl implements MessengerSettingsService {
  private final MessengerSettingsModelRepository repository;

  @Override
  public MessengerSettingsModel get() {
    return repository
        .findById(MessengerSettingsModel.ID)
        .orElseThrow(() -> new IdNotFoundException("MessengerSettingsModel not found"));
  }

  @Override
  public void save(final MessengerSettingsModel model) {
    repository.save(model);
  }
}
