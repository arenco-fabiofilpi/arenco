package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.ContactModel;
import br.com.arenco.arenco_cronjobs.enums.ContactType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactModelRepository extends MongoRepository<ContactModel, String> {
  List<ContactModel> findByUserModelId(final String userModelId);

  Optional<ContactModel> findFirstByUserModelIdAndContactTypeOrderByDateCreatedDesc(
      final String userModelId, final ContactType contactType);

  boolean existsByValueAndUserModelIdAndContactType(
      final String value, final String userModelId, final ContactType contactType);

  boolean existsByUserModelIdAndValueAndContactType(
      final String userModelId, final String value, final ContactType contactType);
}
