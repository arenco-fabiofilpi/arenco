package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.LoginOtpModel;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginOtpModelRepository extends MongoRepository<LoginOtpModel, String> {
  Optional<LoginOtpModel> findFirstByUserModelId(final String userModelId);
}
