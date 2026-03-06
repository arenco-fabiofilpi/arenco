package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.PasswordRecoveryModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordRecoveryModelRepository
    extends MongoRepository<PasswordRecoveryModel, String> {
  Optional<PasswordRecoveryModel> findFirstByUserAgentAndIpAndUserIdOrderByDateCreatedDesc(
      final String userAgent, final String ip, final String userId);
}
