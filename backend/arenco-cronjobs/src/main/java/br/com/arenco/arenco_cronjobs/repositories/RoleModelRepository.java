package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.RoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleModelRepository extends MongoRepository<RoleModel, String> {
  Optional<RoleModel> findByCode(String code);

  boolean existsByCode(final String code);
}
