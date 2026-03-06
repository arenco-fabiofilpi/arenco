package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.MessengerSettingsModel;
import br.com.arenco.dataseeder.enums.AutomaticPaymentSendState;
import br.com.arenco.dataseeder.enums.AutomaticPaymentSendStrategy;
import br.com.arenco.dataseeder.enums.AutomaticPaymentTargetUsers;
import br.com.arenco.dataseeder.repositories.MessengerSettingsModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedMessengerSettingsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "configuracoesDeMensageria";

  private final MessengerSettingsModelRepository repository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var preExistentModelList = repository.findAll();
    if (!preExistentModelList.isEmpty()) {
      log.info("Messenger settings found");
      return;
    }
    final var messengerSettingsModel = new MessengerSettingsModel();
    final var stateAsString = doc.get("state").asText();
    messengerSettingsModel.setState(AutomaticPaymentSendState.valueOf(stateAsString));
    final var strategyAsString = doc.get("strategy").asText();
    messengerSettingsModel.setStrategy(AutomaticPaymentSendStrategy.valueOf(strategyAsString));
    final var targetUsersAsString = doc.get("targetUsers").asText();
    messengerSettingsModel.setTargetUsers(AutomaticPaymentTargetUsers.valueOf(targetUsersAsString));
    repository.save(messengerSettingsModel);
  }
}
