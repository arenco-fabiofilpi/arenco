package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceivedTitleModelRepository
    extends MongoRepository<@NonNull ReceivedTitleModel, @NonNull String> {
  Optional<ReceivedTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);

  List<ReceivedTitleModel> findByCliente(final String cliente);
}
