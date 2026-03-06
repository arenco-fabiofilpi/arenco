package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupModelRepository extends MongoRepository<UserGroupModel, String> {
  Optional<UserGroupModel> findByCode(final String code);

  boolean existsByCode(final String code);
}
