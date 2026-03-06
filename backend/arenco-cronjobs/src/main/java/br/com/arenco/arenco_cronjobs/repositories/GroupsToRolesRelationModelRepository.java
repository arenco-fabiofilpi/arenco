package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.GroupsToRolesRelationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupsToRolesRelationModelRepository
    extends MongoRepository<GroupsToRolesRelationModel, String> {
  List<GroupsToRolesRelationModel> findByUserGroupId(final String userGroupId);

  boolean existsByUserGroupIdAndRoleId(final String userGroupId, final String roleId);
}
