package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.ContactPreferenceModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactPreferenceModelRepository
    extends MongoRepository<ContactPreferenceModel, String> {
  Optional<ContactPreferenceModel> findFirstByUserModelIdOrderByDateCreatedDesc(
      final String userModelId);

  boolean existsByUserModelIdAndContactModelId(
      final String userModelId, final String contactModelId);
}
