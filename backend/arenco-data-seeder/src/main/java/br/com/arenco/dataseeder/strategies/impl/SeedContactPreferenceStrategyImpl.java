package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.ContactPreferenceModel;
import br.com.arenco.dataseeder.enums.ContactType;
import br.com.arenco.dataseeder.repositories.ContactModelRepository;
import br.com.arenco.dataseeder.repositories.ContactPreferenceModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedContactPreferenceStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "preferenciaDeContato";

  private final ContactPreferenceModelRepository contactPreferenceModelRepository;
  private final ContactModelRepository contactModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(JsonNode doc) {
    final var userModel = getUserModel(doc);
    final var contactTypeAsString = doc.path("contactModel").path("$type").asText();
    final ContactType contactType = ContactType.valueOf(contactTypeAsString);
    final var contactModelOpt =
        contactModelRepository.findFirstByUserModelIdAndContactTypeOrderByDateCreatedDesc(
            userModel.getId(), contactType);
    if (contactModelOpt.isEmpty()) {
      log.error(
          "Nao foi encontrado Contato do tipo {} para o usuario {}",
          contactType,
          userModel.getId());
      return;
    }
    final var contactModel = contactModelOpt.get();
    final var exists =
        contactPreferenceModelRepository.existsByUserModelIdAndContactModelId(
            userModel.getId(), contactModel.getId());
    if (exists) {
      log.info("Preferencia de contato ja existente");
      return;
    }

    final var model = new ContactPreferenceModel();
    model.setContactModelId(contactModel.getId());
    model.setUserModelId(userModel.getId());

    contactPreferenceModelRepository.save(model);
  }
}
