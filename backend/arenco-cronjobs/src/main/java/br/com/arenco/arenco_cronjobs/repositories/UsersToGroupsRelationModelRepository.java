package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.UsersToGroupsRelationModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersToGroupsRelationModelRepository
    extends MongoRepository<UsersToGroupsRelationModel, String> {
  List<UsersToGroupsRelationModel> findByUserId(final String id);

  boolean existsByUserIdAndUserGroupId(final String userId, final String userGroupId);
}
