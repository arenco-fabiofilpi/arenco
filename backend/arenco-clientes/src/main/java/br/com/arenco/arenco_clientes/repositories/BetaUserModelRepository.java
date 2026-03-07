package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.BetaUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetaUserModelRepository extends MongoRepository<BetaUserModel, String> {
  boolean existsByUserModelId(final String userModelId);
}
