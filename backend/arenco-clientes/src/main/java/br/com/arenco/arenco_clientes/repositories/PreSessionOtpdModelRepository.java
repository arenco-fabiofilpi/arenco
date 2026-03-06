package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PreSessionOtpdModelRepository
    extends MongoRepository<PreSessionOtpModel, String> {
  Optional<PreSessionOtpModel> findByPreSessionId(final String preSessionId);
}
