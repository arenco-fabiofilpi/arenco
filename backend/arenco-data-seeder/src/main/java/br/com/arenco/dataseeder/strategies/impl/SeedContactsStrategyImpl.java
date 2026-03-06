package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.ContactModel;
import br.com.arenco.dataseeder.enums.ContactType;
import br.com.arenco.dataseeder.repositories.ContactModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedContactsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "contatos";

  private final ContactModelRepository contactModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var contactTypeAsString = doc.get("type").asText();
    final ContactType contactType = ContactType.valueOf(contactTypeAsString);
    final var contactValue = doc.path("value").asText();
    final var userModel = getUserModel(doc);
    final var exists =
        contactModelRepository.existsByUserModelIdAndValueAndContactType(
            userModel.getId(), contactValue, contactType);
    if (exists) {
      log.info("Contato ja existente");
      return;
    }

    final var model = new ContactModel();
    model.setContactType(contactType);
    model.setValue(contactValue);
    model.setUserModelId(userModel.getId());

    contactModelRepository.save(model);
  }
}
