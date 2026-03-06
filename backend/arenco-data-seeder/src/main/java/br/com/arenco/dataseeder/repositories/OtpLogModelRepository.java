package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.OtpLogModel;
import br.com.arenco.dataseeder.enums.OtpType;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpLogModelRepository extends MongoRepository<OtpLogModel, String> {
  Optional<OtpLogModel> findFirstByUserModelIdAndDeliveredToAndTypeOrderByExpiresAtAsc(
      final String userModelId, final String deliveredTo, final OtpType type);
}
