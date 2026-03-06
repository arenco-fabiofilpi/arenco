package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.PasswordRecoveryModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordRecoveryModelRepository
    extends MongoRepository<PasswordRecoveryModel, String> {
  Optional<PasswordRecoveryModel> findFirstByUserAgentAndIpAndUserIdOrderByDateCreatedDesc(
      final String userAgent, final String ip, final String userId);
}
