package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.CadClientesSocioModel;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesSocioModelRepository
    extends MongoRepository<@NonNull CadClientesSocioModel, @NonNull String> {
  List<CadClientesSocioModel> findAllByCliente(@NonNull final Integer cliente);
}
