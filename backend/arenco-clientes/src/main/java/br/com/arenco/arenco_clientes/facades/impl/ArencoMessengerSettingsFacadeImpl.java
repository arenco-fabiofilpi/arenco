package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.dtos.settings.MessengerSettingsDto;
import br.com.arenco.arenco_clientes.factories.MessengerSettingsDtoFactory;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendState;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendStrategy;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentTargetUsers;
import br.com.arenco.arenco_clientes.dtos.UpdateMessengerSettingsDto;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerSettingsFacade;
import br.com.arenco.arenco_clientes.services.MessengerSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoMessengerSettingsFacadeImpl implements ArencoMessengerSettingsFacade {
  private final MessengerSettingsService service;

  @Override
  public MessengerSettingsDto getSettings() {
    final var model = service.get();
    return new MessengerSettingsDtoFactory(model).create();
  }

  @Override
  public void updateSettings(final UpdateMessengerSettingsDto updateSettingsDto) {
    final var model = service.get();
    boolean updated = false;
    if (updateSettingsDto.state() != null && model.getState() != updateSettingsDto.state()) {
      log.info(
          "Alterando Estado da Configuração de {} para {}",
          model.getState(),
          updateSettingsDto.state());
      model.setState(updateSettingsDto.state());
      updated = true;
    }

    if (updateSettingsDto.strategy() != null
        && model.getStrategy() != updateSettingsDto.strategy()) {
      log.info(
          "Alterando Estratégia da Configuração de {} para {}",
          model.getStrategy(),
          updateSettingsDto.strategy());
      model.setStrategy(updateSettingsDto.strategy());
      updated = true;
    }

    if (updateSettingsDto.targetUsers() != null
        && model.getTargetUsers() != updateSettingsDto.targetUsers()) {
      log.info(
          "Alterando Usuários Alvos da Configuração de {} para {}",
          model.getTargetUsers(),
          updateSettingsDto.targetUsers());
      model.setTargetUsers(updateSettingsDto.targetUsers());
      updated = true;
    }

    if (updated) {
      service.save(model);
    }
  }
}
