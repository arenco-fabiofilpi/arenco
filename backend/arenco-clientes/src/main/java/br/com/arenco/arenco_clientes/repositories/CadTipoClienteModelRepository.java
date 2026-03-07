package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.CadTipoClienteModel;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadTipoClienteModelRepository
    extends MongoRepository<@NonNull CadTipoClienteModel, @NonNull String> {
  Optional<CadTipoClienteModel> findByCliente(@NonNull final Integer cliente);
}
