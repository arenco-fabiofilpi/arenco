package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.OtpLogModel;
import br.com.arenco.arenco_cronjobs.enums.OtpType;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpLogModelRepository extends MongoRepository<OtpLogModel, String> {
  Optional<OtpLogModel> findFirstByUserModelIdAndDeliveredToAndTypeOrderByExpiresAtAsc(
      final String userModelId, final String deliveredTo, final OtpType type);
}
