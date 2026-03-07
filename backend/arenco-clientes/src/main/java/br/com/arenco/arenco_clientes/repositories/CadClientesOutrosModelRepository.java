package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.CadClientesOutrosModel;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesOutrosModelRepository
    extends MongoRepository<@NonNull CadClientesOutrosModel, @NonNull String> {
  Optional<CadClientesOutrosModel> findByCliente(@NonNull final Integer cliente);
}
