package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.ReceivedTitleModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceivedTitleModelRepository extends MongoRepository<ReceivedTitleModel, String> {
  Optional<ReceivedTitleModel> findByContratoIdAndSequencia(
      final String contratoId, final String sequencia);

  boolean existsByContratoIdAndSequencia(final String contratoId, final String sequencia);
}
