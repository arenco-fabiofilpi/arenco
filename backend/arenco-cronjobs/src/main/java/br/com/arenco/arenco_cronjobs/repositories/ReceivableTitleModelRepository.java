package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.ReceivableTitleModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReceivableTitleModelRepository
    extends MongoRepository<ReceivableTitleModel, String> {
  int countAllByContratoId(final String contratoId);

  List<ReceivableTitleModel> findByContratoId(final String contratoId, final Pageable pageable);

  Optional<ReceivableTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);
}
