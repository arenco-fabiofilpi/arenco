package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.AddressModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressModelRepository extends MongoRepository<AddressModel, String> {
  boolean existsByStreetNameAndStreetNumberAndCepAndUserId(
      final String streetName, final String streetNumber, final String cep, final String userId);

  Optional<AddressModel> findFirstByUserId(final String userId);
}
