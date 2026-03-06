package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.RoleModel;
import br.com.arenco.dataseeder.repositories.RoleModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedPermissionsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "permissao";

  private final RoleModelRepository roleModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final String code = doc.get("code").asText();

    if (roleModelRepository.existsByCode(code)) {
      log.info("RoleModel ja existente.");
      return;
    }

    var role = new RoleModel();
    role.setCode(doc.get("code").asText());
    role.setName(doc.get("name").asText());

    roleModelRepository.save(role);
  }
}
