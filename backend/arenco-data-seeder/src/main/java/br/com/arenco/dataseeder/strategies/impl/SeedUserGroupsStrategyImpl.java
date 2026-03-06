package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.UserGroupModel;
import br.com.arenco.dataseeder.repositories.UserGroupModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedUserGroupsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "grupoDeUsuario";

  private final UserGroupModelRepository userGroupModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final String code = doc.get("code").asText();

    if (userGroupModelRepository.existsByCode(code)) {
      log.info("UserGroup ja existente.");
      return;
    }

    final var grupo = new UserGroupModel();
    grupo.setCode(doc.get("code").asText());
    grupo.setName(doc.get("name").asText());

    userGroupModelRepository.save(grupo);
  }
}
