package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.GroupsToSuperGroupsRelationModel;
import br.com.arenco.dataseeder.repositories.GroupsToSuperGroupsRelationModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedGroupHierarchyRelationsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "relacaoGruposESuperGrupos";
  private final GroupsToSuperGroupsRelationModelRepository
      groupsToSuperGroupsRelationModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(JsonNode doc) {
    final var superGrupo = getGroupModel(doc, "superGrupoModel");
    final var grupo = getGroupModel(doc);

    final var exists =
        groupsToSuperGroupsRelationModelRepository.existsByGroupIdAndSuperGroupId(
            grupo.getId(), superGrupo.getId());
    if (exists) {
      log.info("Relação Grupo/SuperGrupo ja existente.");
      return;
    }

    final var rel = new GroupsToSuperGroupsRelationModel();
    rel.setGroupId(grupo.getId());
    rel.setSuperGroupId(superGrupo.getId());

    groupsToSuperGroupsRelationModelRepository.save(rel);
  }
}
