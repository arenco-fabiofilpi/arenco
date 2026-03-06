package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.RoleModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleModelRepository extends MongoRepository<RoleModel, String> {
  Optional<RoleModel> findByCode(String code);

  boolean existsByCode(final String code);
}
