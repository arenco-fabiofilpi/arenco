package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.ReceivedTitleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReceivedTitleModelRepository extends MongoRepository<ReceivedTitleModel, String> {
  Optional<ReceivedTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);
}
