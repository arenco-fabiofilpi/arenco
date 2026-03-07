package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.CadClientesRefBancariasModel;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesRefBancariasModelRepository
    extends MongoRepository<@NonNull CadClientesRefBancariasModel, @NonNull String> {
  List<CadClientesRefBancariasModel> findAllByCliente(@NonNull final Integer cliente);
}
