package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.GroupsToSuperGroupsRelationModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupsToSuperGroupsRelationModelRepository
    extends MongoRepository<GroupsToSuperGroupsRelationModel, String> {
  List<GroupsToSuperGroupsRelationModel> findByGroupId(final String userGroupId);

  List<GroupsToSuperGroupsRelationModel> findBySuperGroupId(final String superGroupId);

  boolean existsByGroupIdAndSuperGroupId(final String groupId, final String superGroupId);
}
