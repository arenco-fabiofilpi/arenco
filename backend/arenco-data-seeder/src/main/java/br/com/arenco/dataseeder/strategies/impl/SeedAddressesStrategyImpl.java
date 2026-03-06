package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.AddressModel;
import br.com.arenco.dataseeder.repositories.AddressModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedAddressesStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "enderecos";
  private final AddressModelRepository addressModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var userModel = getUserModel(doc);
    final var streetName = doc.get("streetName").textValue();
    final var streetNumber = doc.get("streetNumber").textValue();
    final var cep = doc.get("cep").textValue();
    final var userId = userModel.getId();
    final var alreadyExists =
        addressModelRepository.existsByStreetNameAndStreetNumberAndCepAndUserId(
            streetName, streetNumber, cep, userId);
    if (alreadyExists) {
      log.info("Address already exists");
      return;
    }
    final var addressModel = new AddressModel();
    addressModel.setStreetName(streetName);
    addressModel.setStreetNumber(streetNumber);
    addressModel.setDistrict(doc.get("district").asText());
    addressModel.setCity(doc.get("city").asText());
    addressModel.setCep(cep);
    addressModel.setState(doc.get("state").asText());
    addressModel.setCountry(doc.get("country").asText());
    addressModel.setDisabled(doc.get("disabled").asBoolean());
    addressModel.setUserId(userId);

    addressModelRepository.save(addressModel);
  }
}
