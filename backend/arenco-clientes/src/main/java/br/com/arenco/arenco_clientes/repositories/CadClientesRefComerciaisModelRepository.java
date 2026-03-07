package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.CadClientesRefComerciaisModel;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesRefComerciaisModelRepository
    extends MongoRepository<@NonNull CadClientesRefComerciaisModel, @NonNull String> {
  List<CadClientesRefComerciaisModel> findAllByCliente(@NonNull final Integer cliente);
}
