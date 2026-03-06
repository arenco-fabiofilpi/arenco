package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.ContactPreferenceModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContactPreferenceModelRepository
    extends MongoRepository<ContactPreferenceModel, String> {
  Optional<ContactPreferenceModel> findFirstByUserModelIdOrderByDateCreatedDesc(
      final String userModelId);

  boolean existsByUserModelIdAndContactModelId(
      final String userModelId, final String contactModelId);
}
