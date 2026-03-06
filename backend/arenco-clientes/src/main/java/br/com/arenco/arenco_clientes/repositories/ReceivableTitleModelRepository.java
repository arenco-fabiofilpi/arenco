package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceivableTitleModelRepository
    extends MongoRepository<ReceivableTitleModel, String> {
  int countAllByContratoId(final String contratoId);

  List<ReceivableTitleModel> findByContratoId(final String contratoId, final Pageable pageable);

  Optional<ReceivableTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);
}
