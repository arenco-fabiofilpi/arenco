package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.ReceivedTitleModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceivedTitleModelRepository extends MongoRepository<ReceivedTitleModel, String> {
  Optional<ReceivedTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);
}
