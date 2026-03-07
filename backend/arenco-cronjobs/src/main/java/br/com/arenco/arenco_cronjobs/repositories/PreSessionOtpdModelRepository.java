package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.PreSessionOtpModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PreSessionOtpdModelRepository
    extends MongoRepository<PreSessionOtpModel, String> {
  Optional<PreSessionOtpModel> findByPreSessionId(final String preSessionId);
}
