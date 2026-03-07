package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.GroupsToRolesRelationModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupsToRolesRelationModelRepository
    extends MongoRepository<GroupsToRolesRelationModel, String> {
  List<GroupsToRolesRelationModel> findByUserGroupId(final String userGroupId);

  boolean existsByUserGroupIdAndRoleId(final String userGroupId, final String roleId);
}
