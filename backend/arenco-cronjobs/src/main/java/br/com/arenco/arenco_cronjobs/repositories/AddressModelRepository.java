package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.AddressModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AddressModelRepository extends MongoRepository<AddressModel, String> {
  boolean existsByStreetNameAndStreetNumberAndCepAndUserId(
      final String streetName, final String streetNumber, final String cep, final String userId);

  Optional<AddressModel> findFirstByUserId(final String userId);
}
