package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.UsersToGroupsRelationModel;
import br.com.arenco.dataseeder.repositories.UsersToGroupsRelationModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedUsersAndGroupsRelationStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "relacaoUsuariosEGrupos";

  private final UsersToGroupsRelationModelRepository usersToGroupsRelationModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var grupo = getGroupModel(doc);
    final var usuario = getUserModel(doc);
    if (usersToGroupsRelationModelRepository.existsByUserIdAndUserGroupId(
        usuario.getId(), grupo.getId())) {
      log.info("Relacao Grupo/Usuario ja existente");
      return;
    }

    final var rel = new UsersToGroupsRelationModel();
    rel.setUserGroupId(grupo.getId());
    rel.setUserId(usuario.getId());

    usersToGroupsRelationModelRepository.save(rel);
  }
}
