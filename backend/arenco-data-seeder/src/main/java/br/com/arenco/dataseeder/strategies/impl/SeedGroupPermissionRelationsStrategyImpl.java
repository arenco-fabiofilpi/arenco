package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.GroupsToRolesRelationModel;
import br.com.arenco.dataseeder.entities.RoleModel;
import br.com.arenco.dataseeder.repositories.GroupsToRolesRelationModelRepository;
import br.com.arenco.dataseeder.repositories.RoleModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedGroupPermissionRelationsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "relacaoGruposEPermissoes";

  private final GroupsToRolesRelationModelRepository groupsToRolesRelationModelRepository;
  private final RoleModelRepository roleModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var grupo = getGroupModel(doc);
    final var permissao = getRoleModel(doc);
    final var exists =
        groupsToRolesRelationModelRepository.existsByUserGroupIdAndRoleId(
            grupo.getId(), permissao.getId());
    if (exists) {
      log.info("Relação Grupo/Role ja existente.");
      return;
    }

    final var rel = new GroupsToRolesRelationModel();
    rel.setUserGroupId(grupo.getId());
    rel.setRoleId(permissao.getId());

    groupsToRolesRelationModelRepository.save(rel);
  }

  private RoleModel getRoleModel(final JsonNode doc) {
    final String permissaoCode = doc.path("permissaoModel").path("$code").asText();

    final var permissaoOpt = roleModelRepository.findByCode(permissaoCode);

    if (permissaoOpt.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(
              "Relação grupo-permissão ignorada: permissão %s não encontrados", permissaoCode));
    }
    return permissaoOpt.get();
  }
}
