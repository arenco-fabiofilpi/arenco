package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.UsersToGroupsRelationModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersToGroupsRelationModelRepository
    extends MongoRepository<UsersToGroupsRelationModel, String> {
  List<UsersToGroupsRelationModel> findByUserId(final String id);

  boolean existsByUserIdAndUserGroupId(final String userId, final String userGroupId);
}
