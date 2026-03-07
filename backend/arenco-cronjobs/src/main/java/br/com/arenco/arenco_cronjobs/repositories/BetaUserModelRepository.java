package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.BetaUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetaUserModelRepository extends MongoRepository<BetaUserModel, String> {
  boolean existsByUserModelId(final String userModelId);
}
