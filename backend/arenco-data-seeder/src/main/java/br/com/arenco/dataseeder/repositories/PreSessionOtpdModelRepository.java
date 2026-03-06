package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.PreSessionOtpModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PreSessionOtpdModelRepository
    extends MongoRepository<PreSessionOtpModel, String> {
  Optional<PreSessionOtpModel> findByPreSessionId(final String preSessionId);
}
